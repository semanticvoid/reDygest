#!/usr/bin/perl

my %top_node_ids = ();

my $top_node_file = shift @ARGV;
open FILE, "<$top_node_file" or die;
my $i = 0;
my $max_id = 0;
while(<FILE>) {
	chomp;
	$i++;
	my ($id, $text, $pg) = split("\t", $_);
	$max_id = ($id > $max_id) ? $id : $max_id;
	$top_node_ids{$id} = 1;
	last if $i > 136;
}

my $graph_file = shift @ARGV;
open FILE, "<$graph_file" or die;
my $i = 0;
print $str = "";
while(<FILE>) {
	chomp;
	$i++;
	if($i > 2) {
		my @tokens = split("[ ]+", $_);
		if(!exists $top_node_ids{$tokens[0]} or
			!exists $top_node_ids{$tokens[1]}) {
			next;
		}
		$str .= "$_\n";
	}
}

$str = "*vertices " . $max_id . "\n*edges\n" . $str;

open FILE, ">graph.filter" or die $!;
print FILE "$str\n";
close FILE;

`cp graph.filter /tmp/graph`;
