use JSON::Syck;

while(<>) {
	#chomp;
	my $t;
	eval {
		$t = JSON::Syck::Load($_);
	};
	next if $@;
	
	print $t->{'text'} . "\n";
}
