use Data::Dumper;

my $pg_file = shift @ARGV;
my $node_file = shift @ARGV;
my $tweet_file = shift @ARGV;

my %pagerank = ();
my %eids = ();

my %id_score = ();

my %tweet_score = ();

print "reading pagerank ..... ";
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
print "\t[DONE]\n";

print "reading eids ..... ";
open FILE, "<$node_file" or die;
while(<FILE>) {
	chomp;
	my @tokens = split("[ ']+", $_);
	my $l = @tokens;
	$eids{$tokens[0]} = join(' ', @tokens[1..$l]);
}
close FILE;
print "\t[DONE]\n";

#for my $key (sort { $pagerank{$b} <=> $pagerank{$a} }  keys %pagerank) {
for my $key (keys %pagerank) {
	#print "$key\t" . $eids{$key} . "\t" . $pagerank{$key} . "\n";
	$id_score{$eids{$key}} = $pagerank{$key};
}

print "reading tweets ...... ";
my $index = 0;
open FILE,"<$tweet_file" or die $!;
while(<FILE>) {
	chomp;
	my $score = 0;
	my $tweet = $_;
	for my $key (keys %id_score) {
		eval {
			if($tweet =~ /$key/) {
				$score += $id_score{$key};
			}
		};
		next if $@;
	}
	$tweet_score{$tweet}{'score'} = $score;
	$tweet_score{$tweet}{'index'} = $index++;
	$tweet_score{$tweet}{'tweet'} = $tweet;
}
close FILE;
print "\t[DONE]\n";

my @top_tweets = ();
for my $key (sort { $tweet_score{$b}->{'score'} <=> $tweet_score{$a}->{'score'} } keys %tweet_score) {
	#print $tweet_score{$key} . "\t" . $key . "\n";
	push @top_tweets, $tweet_score{$key};
	#print Dumper($tweet_score{$key});
	last if @top_tweets == 2000;
	#print $tweet_score{$key}{'index'} . "\t" . $tweet_score{$key}{'score'} . "\t" . $tweet_score{$key}{'tweet'} . "\n";
}

my @final_tweets = ();


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
