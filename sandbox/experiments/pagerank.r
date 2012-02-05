library(igraph)
g <- read.graph("/Users/semanticvoid/projects/reDygest/sandbox/experiments/graph", format="pajek")
pg <- page.rank(g)
write.table( pg, "/Users/semanticvoid/Desktop/test.txt")
