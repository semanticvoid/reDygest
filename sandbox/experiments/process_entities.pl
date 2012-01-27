my %entities = ();
my $eid = 1;

my $edges = "";

my $feid = -1;
while(<>) {
	chomp;
	my @tokens = split("[:\[,\]+");
	if($_ =~ /^Entity/) {
		my $entity = $tokens[1];
		$entity =~ s/ $//g;
		$entity =~ s/^ //g;
		if(!exists $entities{$entity}) {
			$entities{$entity} = $eid++;
		}
		$feid = $entities{$entity};
	} elsif($_ =~ /^Co-occurances/) {
		for(my $i=1; $i<@tokens; $i+=2) {
			my $entity = $tokens[$i];
			$entity =~ s/ $//g;
			$entity =~ s/^ //g;
			if(!exists $entities{$entity}) {
				$entities{$entity} = $eid++;
			}
			my $teid = $entities{$entity};
			$edges .= "$feid $teid\n";
		}
	}
}

print "*vertices " . (keys %entities) . "\n";
open FILE, ">eids" or die;
for my $key (keys %entities) {
	print FILE $entities{$key} . " '" . $key . "'\n";
}
close FILE;

print "*edges\n";
print $edges;
