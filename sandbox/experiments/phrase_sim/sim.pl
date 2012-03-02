#!/usr/bin/perl

use Data::Dumper;
use WordNet::QueryData;
use WordNet::Similarity;
use WordNet::Similarity::vector;
use Lingua::EN::Tagger;
use Math::Matrix;

# wordnet
my $wn = WordNet::QueryData->new( noload => 1);
# similarity
my $measure = WordNet::Similarity::vector->new ($wn);
# tagger
my $ptag = new Lingua::EN::Tagger;

# phrases
my $phrase_1 = shift @ARGV;
my $phrase_2 = shift @ARGV;
my %p1_words = ();
my %p2_words = ();

# tag
my %tmp = $ptag->get_words($phrase_1);
%p1_words = map { lc($_) => 1 } (keys %tmp);
%tmp = $ptag->get_words($phrase_2);
%p2_words = map { lc($_) => 1 } (keys %tmp);
%tmp = get_verbs($ptag->add_tags($phrase_1));
%p1_words = (%p1_words, %tmp);
%tmp = get_verbs($ptag->add_tags($phrase_2));
%p2_words = (%p2_words, %tmp);

my @W_rows = ();

my %all_words = (%p1_words, %p2_words);

print Dumper(keys %all_words);

# populate W
for my $w1 (keys %all_words) {
	my @row = ();
	for my $w2 (keys %all_words) {
		my $sim = get_sim($w1, $w2);
		push @row, $sim;
	}
	push @W_rows, \@row;
}

my @a = ();
my $a_denom = 0;
for my $w (keys %all_words) {
	if(exists $p1_words{$w}) {
		push @a, 1;
		$a_denom++;
	} else {
		push @a, 0;
	}
}

my @b = ();
my $b_denom = 0;
for my $w (keys %all_words) {
	if(exists $p2_words{$w}) {
		push @b, 1;
		$b_denom++;
	} else {
		push @b, 0;
	}
}

# score
my @int = ();
my $r = 0;
my $y = 0;
my @avg = ();
for my $row (@W_rows) {
	my $i = $a[$r];
	for(my $x=0; $x<@{$row}; $x++) {
		my $j = $row->[$x];
		if($r == 0) {
			my $t = $i*$j;
			if($t > 0) {
				push @avg, 1;
			} else {
				push @avg, 0;
			}
			push @int, $t;
		} else {
			my $t = $i*$j;
			$int[$x] = $int[$x]+$t;
			if($t > 0) {
				$avg[$x] += 1;
			}
		}
	}
	$r++;
}

my $num = 0;
for(my $i=0; $i<@int; $i++) {
	$num += ($int[$i]/$avg[$i])*$b[$i] if $avg[$i] > 0;
}
my $denom = sqrt($a_denom) * sqrt($b_denom);
my $score = $num/$denom;
#$score = 0 if $score > 1;
print "score: " . $score . "\n";

########## functions ##########

sub get_sim {
	my ($w1, $w2) = @_;
	return 1 if $w1 eq $w2;
	my @pos1 = $wn->querySense($w1);
	my @pos2 = $wn->querySense($w2);
	if(!@pos1 or !@pos2) {
		return 0;
	}

	my $max_sim = 0;
	for my $p1 (@pos1) {
		my @senses1 = $wn->querySense($p1);
		next if !@senses1;
		for my $p2 (@pos2) {
			my @senses2 = $wn->querySense($p2);
			next if !@senses2;
			
			for my $s1 (@senses1) {
				for my $s2 (@senses2) {
					my $value = $measure->getRelatedness($s1, $s2);
					$max_sim = $value if $value > $max_sim;
				}
			}
		}
	}
	
	return $max_sim;
}

sub get_verbs {
	my ($tagged_sentence) = @_;
	my %verbs = ();
	my @tokens = split("[ \t]+", $tagged_sentence);
	for my $t (@tokens) {
		if($t =~ "^<v.*>(.+)<") {
			$verbs{lc($1)} = 1;
		}
	}

	return %verbs;
}
