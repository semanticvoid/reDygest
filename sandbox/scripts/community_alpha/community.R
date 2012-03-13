library('igraph')
g <- read.graph("/tmp/graph.top", "pajek")
cs <- leading.eigenvector.community(as.undirected(g), steps=200)
write(cs$membership, "/tmp/membership")
