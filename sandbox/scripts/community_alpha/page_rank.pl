#!/usr/bin/perl

# script to compute pagerank
# usage: perl page_rank.pl <tweets json file>

use Data::Dumper;
use JSON::Syck;

my $file = undef;
my %entities = ();
my $eid = 1;
my $edges = "";
my $feid = -1;

# check args
if(@ARGV == 0) {
	print "usage: perl page_rank.pl <tweets json file>\n";
	exit;
} else {
	$file = shift @ARGV;
}

# read json file
open FILE, "<$file" or die $!;
while(<FILE>) {
	chomp;
	
	my $tweet = undef;
	eval {
		$tweet = JSON::Syck::Load($_);
	};
	next if $@;	

	my @tweet_entities = @{$tweet->{'entities'}} 
		if exists $tweet->{'entities'};	
	
	if(@tweet_entities) {
		for my $e1 (@tweet_entities) {
			my $e1_id;
			if(exists $entities{$e1}) {
				$e1_id = $entities{$e1};
			} else {
				$entities{$e1} = $e1_id = $eid++;
			}
			
			for my $e2 (@tweet_entities) {
				next if $e1 eq $e2;
				
				my $e2_id;
				if(exists $entities{$e2}) {
					$e2_id = $entities{$e2};
				} else {
					$entities{$e2} = $e2_id = $eid++;
				}
				
				$edges .= "$e1_id $e2_id\n";
			}
		}
	}
}

open FILE, ">eids" or die;
for my $key (sort {$entities{$a} <=> $entities{$b}} keys %entities) {
	print FILE $entities{$key} . " '" . $key . "'\n";
}
close FILE;

open FILE, ">graph" or die;
print FILE "*vertices " . (keys %entities) . "\n";
print FILE "*edges\n";
print FILE $edges;
close FILE;

`cp graph /tmp/graph`;
`cp eids /tmp/eids`;
#`R --no-save < pg.R`;
