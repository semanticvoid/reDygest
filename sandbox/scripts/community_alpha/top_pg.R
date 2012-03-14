d <- read.table("/tmp/pagerank.nodes", sep="\t")
write.table(d[which(d$V3 > quantile(d$V3, 0.75) + (IQR(d$V3) * 1.5)), ], "/tmp/top_pagerank.nodes", sep="\t", quote=FALSE, col.names=FALSE, row.names=FALSE )
