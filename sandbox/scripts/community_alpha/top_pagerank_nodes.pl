#!/usr/bin/perl

use Data::Dumper;

my $pg_file = shift @ARGV;
my $node_file = shift @ARGV;

my %pagerank = ();
my %eids = ();

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

open FILE, "<$node_file" or die;
while(<FILE>) {
	chomp;
	my @tokens = split("[ ']+", $_);
	my $l = @tokens;
	$eids{$tokens[0]} = join(' ', @tokens[1..$l]);
}
close FILE;

open FILE, ">top_pagerank.nodes" or die $!; 
for my $key (sort { $pagerank{$b} <=> $pagerank{$a} }  keys %pagerank) {
	print FILE "$key\t" . $eids{$key} . "\t" . $pagerank{$key} . "\n";
}
close FILE;
