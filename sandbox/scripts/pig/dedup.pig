REGISTER '/Users/semanticvoid/.m2/repository/net/sf/json-lib/json-lib/2.3/json-lib-2.3-jdk15.jar';
REGISTER '/Users/semanticvoid/.m2/repository/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar';
REGISTER '/Users/semanticvoid/projects/reDygest/sandbox/piggybank/target/piggybank-0.0.1-SNAPSHOT.jar';

X1 = LOAD '$INPUT' AS (tweetjson);
A = FOREACH X1 GENERATE FLATTEN(com.redygest.piggybank.twitter.ExtractTweet($0));

-- round 1: ndd
C = FOREACH A GENERATE $0 as id, $1 as tweet, com.redygest.piggybank.text.Shingle($1, 2) AS shingles;
D1 = FOREACH C GENERATE id, tweet, com.redygest.piggybank.text.MinHashSketch(shingles, 10) AS sketch;
D2 = FOREACH D1 GENERATE id, tweet, sketch;
F = CROSS D1, D2 PARALLEL 200;
G = FOREACH F GENERATE FLATTEN(com.redygest.piggybank.twitter.GetSmallerId(D1::id, D2::id)) AS key, D1::id, D1::tweet, D2::id, D2::tweet, flatten(com.redygest.piggybank.similarity.JaccardCoeff(D1::sketch, D2::sketch)) AS (sim:double);
H = FILTER G BY (NOT (D1::id == D2::id)) AND ((sim >= 0.5) AND (sim <= 1));
I = GROUP H BY key;
J = FOREACH I GENERATE group, COUNT(H) as count, FLATTEN(H);
K = FOREACH J GENERATE group as id, $4 as tweet, count;
K1 = FILTER K BY count >= $THRESHOLD;
L = GROUP K1 BY id;
M = FOREACH L GENERATE FLATTEN(K1);
N = FOREACH M GENERATE FLATTEN(com.redygest.piggybank.twitter.AddCountToTweet(K1::tweet, K1::count)) as tweet;

-- round 2: exact dup


-- store
STORE N INTO '$OUTPUT';
