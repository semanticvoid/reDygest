<?php

$dbcnx = @mysql_connect("localhost", "root", "");
if (!$dbcnx) {
	echo( "<P>Unable to connect to the " .
		"database server at this time.</P>" );
	exit();
} else {
	mysql_select_db("redygest", $dbcnx);
}

$id = htmlspecialchars($_GET["id"]);

if($id != null) {
	$result = mysql_query("SELECT story_json FROM stories WHERE id = $id");
	if ($result) {
		while ( $row = mysql_fetch_array($result) ) {
			echo $row["story_json"];
		}
	} else {
		echo mysql_error();
	}
}

?>
