library('igraph')
g <- read.graph("/tmp/graph", "pajek")
pg <- page.rank(g)
write.table(pg, "/tmp/pagerank")
