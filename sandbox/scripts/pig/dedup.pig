REGISTER '/Users/semanticvoid/.m2/repository/net/sf/json-lib/json-lib/2.3/json-lib-2.3-jdk15.jar';
REGISTER '/Users/semanticvoid/.m2/repository/net/sf/ezmorph/ezmorph/1.0.6/ezmorph-1.0.6.jar';
REGISTER 'target/piggybank-0.0.1-SNAPSHOT.jar';

X1 = LOAD '$INPUT' AS (tweetjson);
A = FOREACH X1 GENERATE com.redygest.piggybank.twitter.ExtractTweet($0);
DUMP A;
