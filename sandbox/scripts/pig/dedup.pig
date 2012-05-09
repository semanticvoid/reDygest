REGISTER '/Users/semanticvoid/.m2/repository/net/sf/json-lib/json-lib/2.3/json-lib-2.3-jdk15.jar';
REGISTER '/Users/semanticvoid/.m2/repository/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar';
REGISTER '/Users/semanticvoid/projects/reDygest/sandbox/piggybank/target/piggybank-0.0.1-SNAPSHOT.jar';

-- load data
X1 = LOAD '$INPUT' AS (tweetjson);
A = FOREACH X1 GENERATE FLATTEN(com.redygest.piggybank.twitter.ExtractTweet($0));

-- round 1: ndd

-- shingle
C = FOREACH A GENERATE $0 as id, $1 as tweet, com.redygest.piggybank.text.Shingle($1, 2) AS shingles;

-- cross
D1 = FOREACH C GENERATE id, tweet, com.redygest.piggybank.text.MinHashSketch(shingles, 10) AS sketch;
D2 = FOREACH D1 GENERATE *;
E = CROSS D1, D2 PARALLEL 200;
E1 = FILTER E BY D1::id != D2::id;

-- order ids and distinct
F = FOREACH E1 {
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
H = FILTER G BY (NOT (id1 == id2)) AND ((sim >= 0.5) AND (sim <= 1));
I = GROUP H BY id1;
J = FOREACH I GENERATE group, COUNT(H) as count, FLATTEN(H);
J1 = FOREACH J GENERATE group, H::tweet1 as tweet, count;

-- weed out unpopular tweets
K = FILTER J1 BY count >= $THRESHOLD;

-- add count
N = FOREACH K GENERATE FLATTEN(com.redygest.piggybank.twitter.AddCountToTweet(tweet, count)) as tweet;


-- store
STORE N INTO '$OUTPUT';
