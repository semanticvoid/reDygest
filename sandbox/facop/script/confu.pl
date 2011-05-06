#!/usr/bin/perl

use Data::Dumper;

my $labeled = shift @ARGV;
my $predicted = shift @ARGV;

my %labels = ();

open LABEL, $labeled or die $!;
while(<LABEL>) {
	chomp;
	my ($l, $w, $id) = split("[ ]+", $_);
	$labels{$id} = $l;
}

my ($fp, $fn, $tp, $tn) = (0,0,0,0);

open PREDICT, $predicted or die $!;
while(<PREDICT>) {
	my ($l, $w, $id) = split("[ ]+", $_);
	if($l eq '1' and $labels{$id} eq $l) {
		$tp++;
	} else if($l eq '1' and $labels{$id} ne $l) {
		$fp++;
	} else if($l eq '0' and $labels{$id} eq $l) {
		$tn++;
	} else if($l eq '0' and $labels{$id} ne $l) {
		$fn++;
	} 
}

print "fp: $fp\ttp: $tp\tfn: $fn\ttn: $tn\n";
