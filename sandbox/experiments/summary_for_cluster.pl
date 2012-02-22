
my %cluster_entities = ();

print "reading membership file....";
my $membership_file = shift @ARGV;
my $cluster = shift @ARGV;
open FILE, "<$membership_file" or die;
while(<FILE>) {
	chomp;
	my ($cid, $entity) = split("#", $_);
	$cluster_entities{$entity} = 1 if $cid == $cluster;
}
print "\tdone\n";

print "reading pagerank...";
my $pg_file = shift @ARGV;
my %pagerank = ();
open FILE, "<$pg_file" or die;
my $i = 0;
while(<FILE>) {
        chomp;
        next if $i++ == 0;
        $_ =~ s/"//g;
        my @tokens = split("[ ]+", $_);
        $pagerank{$tokens[0]} = $tokens[1];
}
close FILE;
print "\tdone\n";

my $node_file = shift @ARGV;
my %eids = ();
print "reading eids ..... ";
open FILE, "<$node_file" or die;
while(<FILE>) {
        chomp;
        my @tokens = split("[ ']+", $_);
        my $l = @tokens;
        $eids{join(' ', @tokens[1..$l])} = $tokens[0];
}
close FILE;
print "\t[DONE]\n";

print "reading tweets...";
my $tweet_file = shift @ARGV;
my $index = 0;
open FILE,"<$tweet_file" or die $!;
while(<FILE>) {
        chomp;
        my $score = 0;
        my $tweet = $_;
        for my $key (keys %cluster_entities) {
                eval {
                        if($tweet =~ /$key/) {
                                $score += $pagerank{$eids{$key}};
                        }
                };
                next if $@;
        }
        $tweet_score{$tweet}{'score'} = $score;
        $tweet_score{$tweet}{'index'} = $index++;
        $tweet_score{$tweet}{'tweet'} = $tweet;
}
close FILE;
print "\tdone\n";


print "calculating top tweets...";
my @top_tweets = ();
for my $key (sort { $tweet_score{$b}->{'score'} <=> $tweet_score{$a}->{'score'} } keys %tweet_score) {
        #print $tweet_score{$key} . "\t" . $key . "\n";
        push @top_tweets, $tweet_score{$key};
        #print Dumper($tweet_score{$key});
        last if @top_tweets == 2000;
        #print $tweet_score{$key}{'index'} . "\t" . $tweet_score{$key}{'score'} . "\t" . $tweet_score{$key}{'tweet'} . "\n";
}
print "\tdone\n";

my @final_tweets = ();

=doc
print "ranking....";
for my $tweet (@top_tweets) {
        if(@final_tweets == 0) {
                push @final_tweets, $tweet;
                #print $tweet->{'index'} . "\t" . $tweet->{'score'} . "\t" . $tweet->{'tweet'} . "\n";
        } else {
                my $flag = 1;
                for my $f_tweet (@final_tweets) {
                        my $sim = jacsim($f_tweet->{'tweet'}, $tweet->{'tweet'});
                        #print "$sim\n";
                        if($flag and $sim >= 0.3) {
                                $f_tweet->{'score'} += $sim;
                                $flag = 0;
                                last;
                        }
                }
                push @final_tweets, $tweet if $flag;
                #print $tweet->{'index'} . "\t" . $tweet->{'score'} . "\t" . $tweet->{'tweet'} . "\n" if $flag;
        }
}
print "\tdone\n";
=cut

@final_tweets = @top_tweets;

for my $tweet (sort { $b->{'score'} <=> $a->{'score'} } @final_tweets) {
        #print Dumper($tweet);
        print $tweet->{'index'} . "\t" . $tweet->{'score'} . "\t" . $tweet->{'tweet'} . "\n";
}

sub jacsim {
        my ($t1, $t2) = @_;
        my %v1 = map { $_ => 1 } split("[ ]+", $t1);
        my %v2 = map { $_ => 1 } split("[ ]+", $t2);

        my $match = 0;
        for my $k (keys %v1) {
                if(exists $v2{$k}) {
                        $match++;
                }
        }

        if($match == 0) {
                return 0;
        }

        return ($match*2)/((keys %v1) + (keys %v2));
}
