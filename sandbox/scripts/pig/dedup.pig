REGISTER '/Users/semanticvoid/.m2/repository/net/sf/json-lib/json-lib/2.3/json-lib-2.3-jdk15.jar';
REGISTER '/Users/semanticvoid/.m2/repository/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar';
REGISTER '/Users/semanticvoid/projects/reDygest/sandbox/piggybank/target/piggybank-0.0.1-SNAPSHOT.jar';

-- load data
X1 = LOAD '$INPUT' AS (tweetjson);
A = FOREACH X1 GENERATE FLATTEN(com.redygest.piggybank.twitter.ExtractTweet($0));


-- round 1: exact
Z1 = FOREACH A GENERATE FLATTEN(com.redygest.piggybank.twitter.GetText($1)) as text, $1 as tweet;
Z11 = FOREACH Z1 GENERATE FLATTEN(com.redygest.piggybank.text.MD5Hash(text)) as hash, tweet;
Z2 = GROUP Z11 BY hash;
Z3 = FOREACH Z2 GENERATE group, COUNT(Z11) as count, FLATTEN(com.redygest.piggybank.twitter.OneFromBag(Z11));
Z4 = FOREACH Z3 GENERATE $2 as tweet, $1 as count;
Z5 = FILTER Z4 BY count >= $T1;
Z6 = FOREACH Z5 GENERATE FLATTEN(com.redygest.piggybank.twitter.AddCountToTweet(tweet, count)) as tweet;
Z7 = FOREACH Z6 GENERATE FLATTEN(com.redygest.piggybank.twitter.ExtractTweet(tweet));


-- round 2: ndd

-- shingle
C = FOREACH Z7 GENERATE $0 as id, $1 as tweet, com.redygest.piggybank.text.Shingle($1, 2) AS shingles;
-- C1 = FILTER C BY id != NULL;

-- cross
D1 = FOREACH C GENERATE id, tweet, com.redygest.piggybank.text.MinHashSketch(shingles, 10) AS sketch;
D2 = FOREACH D1 GENERATE *;
E = CROSS D1, D2 PARALLEL 200;
-- E1 = FILTER E BY D1::id != D2::id;
E2 = FILTER E BY D1::id >= D2::id;

-- order ids and distinct
F = FOREACH E2 {
        id1 = D1::id;
        id2 = D2::id;
        id3 = D1::id;
        id1 = (id1 > id2 ? id2 : id1);
        id2 = (id3 > id2 ? id3 : id2);
        data1 = D1::tweet;
        data2 = D2::tweet;
        data3 = D1::tweet;
        data1 = (id1 > id2 ? data2 : data1);
        data2 = (id3 > id2 ? data3 : data2);
        sketch1 = D1::sketch;
        sketch2 = D2::sketch;
        sketch3 = D1::sketch;
        sketch1 = (id1 > id2 ? sketch2 : sketch1);
        sketch2 = (id3 > id2 ? sketch3 : sketch2);
        GENERATE id1 as id1, data1 as tweet1, sketch1 as sketch1, id2 as id2, data2 as tweet2, sketch2 as sketch2;
};
F1 = DISTINCT F;

-- similarity
G = FOREACH F1 GENERATE id1, tweet1, id2, tweet2, flatten(com.redygest.piggybank.similarity.JaccardCoeff(sketch1, sketch2)) AS (sim:double);

-- filter by similarity
H = FILTER G BY (sim >= 0.5);
I = GROUP H BY id1;
J = FOREACH I GENERATE FLATTEN(com.redygest.piggybank.twitter.MergeFromBag(H)) as tweet;


-- round 3: exact

Z1 = FOREACH J GENERATE FLATTEN(com.redygest.piggybank.twitter.GetText(tweet)) as text, tweet;
Z11 = FOREACH Z1 GENERATE FLATTEN(com.redygest.piggybank.text.MD5Hash(text)) as hash, tweet;
Z2 = GROUP Z11 BY hash;
Z3 = FOREACH Z2 GENERATE FLATTEN(com.redygest.piggybank.twitter.OneFromBagWithMaxCount(Z11)) as tweet;

-- filter again
F1 = FOREACH Z3 GENERATE tweet, FLATTEN(com.redygest.piggybank.twitter.GetCount(tweet)) as count;
F2 = FILTER F1 BY count >= $T2;
F3 = FOREACH F2 GENERATE tweet;

-- store
STORE F3 INTO '$OUTPUT';
