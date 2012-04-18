REGISTER '/Users/semanticvoid/.m2/repository/net/sf/json-lib/json-lib/2.3/json-lib-2.3-jdk15.jar';
REGISTER '/Users/semanticvoid/.m2/repository/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar';
REGISTER '/Users/semanticvoid/projects/reDygest/sandbox/piggybank/target/piggybank-0.0.1-SNAPSHOT.jar';

X1 = LOAD '$INPUT' AS (tweetjson);
A = FOREACH X1 GENERATE FLATTEN(com.redygest.piggybank.twitter.ExtractTweet($0));
C = FOREACH A GENERATE $0 as id, $1 as text, com.redygest.piggybank.text.Shingle($1, 2) AS shingles;
DUMP C;
