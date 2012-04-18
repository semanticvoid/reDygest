REGISTER '/Users/semanticvoid/.m2/repository/net/sf/json-lib/json-lib/2.3/json-lib-2.3-jdk15.jar';
REGISTER '/Users/semanticvoid/.m2/repository/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar';
REGISTER '/Users/semanticvoid/projects/reDygest/sandbox/piggybank/target/piggybank-0.0.1-SNAPSHOT.jar';

X1 = LOAD '$INPUT' AS (tweetjson);
A = FOREACH X1 GENERATE FLATTEN(com.redygest.piggybank.twitter.ExtractTweet($0));
C = FOREACH A GENERATE $0 as id, $1 as text, com.redygest.piggybank.text.Shingle($1, 2) AS shingles;
D1 = FOREACH C GENERATE id, text, com.redygest.piggybank.text.MinHashSketch(shingles, 10) AS sketch;
D2 = FOREACH D1 GENERATE id, text, sketch;
F = CROSS D1, D2 PARALLEL 200;
G = FOREACH F GENERATE D1::id, D1::text, D2::id, D2::text, flatten(com.redygest.piggybank.similarity.JaccardCoeff(D1::sketch, D2::sketch)) AS (sim:double);
DUMP G;
