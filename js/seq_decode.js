function initCode(num)
{
	for(var i = 0 ; i < 24 ; ++i)
		document.getElementById("checkbox" + num.toString() + "_" + i.toString()).checked = false;
	document.getElementById("adr"+num.toString()).value = "00";
	document.getElementById("microcode"+num.toString()).value = "0x00000000";
}

function fromCode(num) {
	var code = document.getElementById("microcode"+num.toString()).value;

	if(code.length < 10) {
		var l = code.length;
		for(var i = 0  ; i < 10 - l; ++i)
			code += '0';
	}
	else {
		code = code.substring(0,10);
	}
	if(code.substring(0,2) != "0x")
		code = "0x" + code.substring(2, 10);

	document.getElementById("microcode"+num.toString()).value = code;

	var adr = code.substring(8, 10);
	document.getElementById("adr"+num.toString()).value = adr;

	var seqhex = parseInt(code.substring(0, 8));
	for(var i = 0 ; i < 24; ++i) {
		if(seqhex % 2 == 1)
			document.getElementById("checkbox"+num.toString() + "_" + i.toString()).checked = true;
		else
			document.getElementById("checkbox"+num.toString() + "_"  + i.toString()).checked = false;
		seqhex = Math.floor(seqhex / 2);
	}

}

function testfunc() {
	alert("test func");
}

function computeCode(num)
{
	var code = '';

	var adr = document.getElementById("adr"+num.toString());
	code = adr.value + code;

	for(var i = 0 ; i < 6; ++i) {
		var value = 0;
		for(var j = 0 ; j < 4 ; ++j) {
			var chkboxindex = 4 * i + j;
			var chkid = "checkbox" + num.toString() + "_" + chkboxindex.toString();
			var chkbox = document.getElementById(chkid);
			if(chkbox.checked)
				value = value + Math.pow(2,j);
		}
		code = value.toString(16) + code ;
	}
	code = '0x' + code;

	document.getElementById("microcode" + num.toString()).value = code;

}

function onkeyup(e) {
	var code;
	if (!e) var e = window.event; // some browsers don't pass e, so get it from the window
	if (e.keyCode) code = e.keyCode; // some browsers use e.keyCode
	else if (e.which) code = e.which;  // others use e.which

	if (code == 8 || code == 46)
		return false;
}

function openTable(dataid, datasp, dataint) {
    

	console.log("dataid: " + dataid);
	console.log("datasp: " + datasp);
	console.log("dataint: " + dataint);
	console.log("typeof dataint: " + typeof dataint);

	var html = "";
	html += "<div id='genmicrocode' data-id='" + dataid + "' data-sp='" + datasp + "' data-int='" + dataint + "'>";
	html += "<div class=\"table-responsive\">"
	html += "<table id='Microcode" + dataid + "' class='table table-bordered' style='width:100%'>";
	html += "<caption>Microcode generator</caption>";
	html += "<thead><tr class='table-primary'>";

	if (typeof dataint == "number" && dataint == 1) {
		html += "<th colspan='2' style='text-align: center;'><span class=\"material-symbols-outlined\">cancel</span></th>";
		html += "<th colspan='1'>ReadIntAdr</th>\
			<th colspan='1'>ClearIF</th>\
			<th colspan='1'>SetIF</th>\
			<th colspan='1'>INTA</th>";
	}
	else {
		html += "<th colspan='6' style='text-align: center;'><span class=\"material-symbols-outlined\">cancel</span></th>";
	}

	html += "<th colspan='3'>CodeMCount</th>\
		<th colspan='4'>Code UAL</th>";

	if (typeof datasp == "number" && datasp == 1) {
		// Element has this attribute
		html += "<th>SetSP</th><th>ReadSP</th>";
	}
	else {
		html += "<th colspan='2' style='text-align: center;'><span class=\"material-symbols-outlined\">cancel</span></th>";
	}

	html += "<th>SetB</th><th>ReadB</th>\
		<th>SetA</th><th>ReadA</th>\
		<th>SetPC</th><th>ReadPC</th>\
		<th>SetRADM</th>\
		<th>SetMem</th><th>ReadMem</th>\
		<th>@Adr (8 bits)</th>\
		<th>Code microinstruction</th>\
		</tr></thead>";

	html += "<tbody><tr>";

	html += "<td><input type='checkbox' id='checkbox" + dataid + "_23' onclick='computeCode("+ dataid +");' disabled></td>"
	html += "<td><input type='checkbox' id='checkbox" + dataid + "_22' onclick='computeCode("+ dataid +");' disabled></td>"

	if (typeof dataint == "number" && dataint == 1) {
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_21' onclick='computeCode("+ dataid +");'></td>";
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_20' onclick='computeCode("+ dataid +");'></td>";
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_19' onclick='computeCode("+ dataid +");'></td>";
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_18' onclick='computeCode("+ dataid +");'></td>";
	}
	else {
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_21' onclick='computeCode("+ dataid +");' disabled></td>";
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_20' onclick='computeCode("+ dataid +");' disabled></td>";
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_19' onclick='computeCode("+ dataid +");' disabled></td>";
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_18' onclick='computeCode("+ dataid +");' disabled></td>";
	}
	html += "<td><input type='checkbox' id='checkbox" + dataid + "_17' onclick='computeCode("+ dataid +");'></td><!-- CodeMCount -->\
		<td><input type='checkbox' id='checkbox" + dataid + "_16' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_15' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_14' onclick='computeCode("+ dataid +");'></td><!-- UAL -->\
		<td><input type='checkbox' id='checkbox" + dataid + "_13' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_12' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_11' onclick='computeCode("+ dataid +");'></td>";

	var attr = datasp;
	if (typeof attr !== typeof undefined && attr !== 0) {
		html += "<td><input type='checkbox' id='checkbox" + dataid + "_10' onclick='computeCode("+ dataid +");'></td>\
			<td><input type='checkbox' id='checkbox" + dataid + "_9' onclick='computeCode("+ dataid +");'></td>";
	}
	else {

		html += "<td><input type='checkbox' id='checkbox" + dataid + "_10' onclick='computeCode("+ dataid +");' disabled></td>\
			<td><input type='checkbox' id='checkbox" + dataid + "_9' onclick='computeCode("+ dataid +");' disabled></td>";

	}
	html += "<td><input type='checkbox' id='checkbox" + dataid + "_8' onclick='computeCode("+ dataid +");'></td><!-- CodeMCount -->\
		<td><input type='checkbox' id='checkbox" + dataid + "_7' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_6' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_5' onclick='computeCode("+ dataid +");'></td><!-- UAL -->\
		<td><input type='checkbox' id='checkbox" + dataid + "_4' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_3' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_2' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_1' onclick='computeCode("+ dataid +");'></td>\
		<td><input type='checkbox' id='checkbox" + dataid + "_0' onclick='computeCode("+ dataid +");'></td>";

	html += "<td><input type='text' id='adr"+dataid+"' size='2' maxLength='2' value='00' oninput='computeCode("+dataid+");'></td> <!-- Adr -->\
		<td><input type='text' id='microcode"+dataid+"' size='15' maxLength='10' value='0x00000000' onchange='fromCode("+dataid+")'></td>";

	html += "</tr></tbody>";
	html += "</table>";

	html += "<button type='button' class='btn btn-warning' onclick='initCode("+dataid+");'>Reinitialiser le code</button>";
	html += "</div></div>";

	var tableWindow = window.open("", "", "width=800,height=600,popup=true");
	// TODO: The document.write method is deprecated ! However, the code below does not succeed in loading the javascript file for some reasons
/*
 	tableWindow.document.head.innerHTML = `
	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<!-- Material Icons -->
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=cancel" />

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.7.1.slim.min.js" integrity="sha384-5AkRS45j4ukf+JbWAfHL8P4onPA9p0KwwP7pUdjSQA3ss9edbJUJc/XcYAiheSSz" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	<script src=` + window.location + `../js/seq_decode.js"></script>

    <title>Microcode</title>
	`;

	tableWindow.document.body.innerHTML = `
    <h2>Générateur de micro-code</h2>
		` + html + `

`; */

tableWindow.document.write(`
		<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<!-- Material Icons -->
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=cancel" />

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.7.1.slim.min.js" integrity="sha384-5AkRS45j4ukf+JbWAfHL8P4onPA9p0KwwP7pUdjSQA3ss9edbJUJc/XcYAiheSSz" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	<script src="../js/seq_decode.js" crossorigin="anonymous"></script>

    <title>Microcode</title>
	<h2>Générateur de micro-code</h2>
		` + html );

}