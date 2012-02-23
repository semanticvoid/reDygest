#!/usr/bin/perl

# Extract frequent links from dataset

use Data::Dumper;

my %links = ();

while(<>) {
	chomp;
	my @links = grep { $_ =~ /^http/i } split("[ ]+", $_);
	for my $l (@links) {
		$links{$l} = (exists $links{$l}) ? $links{$l}+1 : 1;
	}
}

for my $l (sort {$links{$b} <=> $links{$a}} keys %links) {
	my $c = $links{$l};
	$l =~ s!\\/!/!g;
	print $l . "\t" . $c . "\n";
}
