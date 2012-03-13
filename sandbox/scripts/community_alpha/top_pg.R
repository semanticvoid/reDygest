d <- read.table("/tmp/top_pagerank.nodes", sep="\t")
write(d[which(d$V3 > quantile(d$V3, 0.75) + (IQR(d$V3) * 1.5)), ], "/tmp/top_pagerank.nodes", sep="\t")
