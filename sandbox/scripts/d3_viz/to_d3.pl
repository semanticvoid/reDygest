#!/usr/bin/perl

# script to convert graph.<topic> & eids
# into d3 graph

# sudo cpan i JSON::Syck if missing

use Data::Dumper;
use JSON::Syck;

my %d3_graph = ();
$d3_graph{'nodes'} = ();
$d3_graph{'links'} = ();

my $graph_file = shift @ARGV;
open FILE, "<$graph_file" or die;
my $i = 0;
while(<FILE>) {
	chomp;
	next if $i++ <= 2;
	my @tokens = split("[ \t]+", $_);
	my $edge = {'source' => $tokens[0]*1,
		'target' => $tokens[1]*1,
		'value' => 1};
	push @{$d3_graph{'links'}}, $edge;
}
close FILE;

my %eids = ();
my $eids_file = shift @ARGV;
open FILE, "<$eids_file" or die;
while(<FILE>) {
	chomp;
	my @tokens = split(" '", $_);
	$eids{$tokens[0]} = $tokens[1];
}
close FILE;

# push '0' node
push @{$d3_graph{'nodes'}}, {'name' => 'dummy', 'group' => 1};
for my $id (sort {$a <=> $b} keys %eids) {
	my $node = {'name' => $eids{$id},
		'group' => 1};
	push @{$d3_graph{'nodes'}}, $node;
}

#print Dumper(%d3_graph);

open FILE, ">graph.json" or die;
print FILE JSON::Syck::Dump(\%d3_graph);
close FILE;
