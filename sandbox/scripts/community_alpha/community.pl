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
	$top_node_ids{$id} = $text;
	#last if $i > 105;
}


open CFILE, ">node.community" or die $!;

my $comm_file = shift @ARGV;
open FILE, "<$comm_file" or die;
my $i = 0;
print $str = "";
while(<FILE>) {
	chomp;
	my @tokens = split("[ ]+", $_);
	for my $token (@tokens) {
		$i++;
		if(exists $top_node_ids{$i}) {
			print CFILE $token . "#" . $top_node_ids{$i} . "\n";
		}
	}
}
close FILE;

close CFILE;

