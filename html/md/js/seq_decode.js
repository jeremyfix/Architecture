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

$(document).ready(function () {

	$('div[id="genmicrocode"]').each(function()
		{

			var html = "<div class=\"md-typeset__scrollwrap\"><div class=\"md-typeset__table\"><table id='Microcode" + $(this).data('id') + "'>";
			html += "<caption>Microcode generator</caption>";
			html += "<thead><tr>";
			var attr = $(this).attr('data-int');
			if (typeof attr !== typeof undefined && attr !== false) {
				// Element has this attribute
				console.log("int");
				html += "<th colspan='2'><i class='material-icons'>clear</i></th>";
				html += "<th colspan='1'>ReadIntAdr</th>\
					<th colspan='1'>ClearIF</th>\
					<th colspan='1'>SetIF</th>\
					<th colspan='1'>INTA</th>";
			}
			else {
				html += "<th colspan='6'><i class='material-icons'>clear</i></th>";
			}

			html += "<th colspan='3'>CodeMCount</th>\
				<th colspan='4'>Code UAL</th>";

			var attr = $(this).attr('data-sp');
			if (typeof attr !== typeof undefined && attr !== false) {
				// Element has this attribute
				console.log("sp");
				html += "<th>SetSP</th><th>ReadSP</th>";
			}
			else {
				html += "<th colspan='2'><i class='material-icons'>clear</i></th>";
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

			html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_23' onclick='computeCode("+ $(this).data('id') +");' disabled></td>"
			html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_22' onclick='computeCode("+ $(this).data('id') +");' disabled></td>"
			var attr = $(this).attr('data-int');
			if (typeof attr !== typeof undefined && attr !== false) {
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_21' onclick='computeCode("+ $(this).data('id') +");'></td>";
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_20' onclick='computeCode("+ $(this).data('id') +");'></td>";
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_19' onclick='computeCode("+ $(this).data('id') +");'></td>";
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_18' onclick='computeCode("+ $(this).data('id') +");'></td>";
			}
			else {
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_21' onclick='computeCode("+ $(this).data('id') +");' disabled></td>";
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_20' onclick='computeCode("+ $(this).data('id') +");' disabled></td>";
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_19' onclick='computeCode("+ $(this).data('id') +");' disabled></td>";
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_18' onclick='computeCode("+ $(this).data('id') +");' disabled></td>";
			}
			html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_17' onclick='computeCode("+ $(this).data('id') +");'></td><!-- CodeMCount -->\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_16' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_15' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_14' onclick='computeCode("+ $(this).data('id') +");'></td><!-- UAL -->\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_13' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_12' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_11' onclick='computeCode("+ $(this).data('id') +");'></td>";

			var attr = $(this).attr('data-sp');
			if (typeof attr !== typeof undefined && attr !== false) {
				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_10' onclick='computeCode("+ $(this).data('id') +");'></td>\
					<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_9' onclick='computeCode("+ $(this).data('id') +");'></td>";
			}
			else {

				html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_10' onclick='computeCode("+ $(this).data('id') +");' disabled></td>\
					<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_9' onclick='computeCode("+ $(this).data('id') +");' disabled></td>";

			}
			html += "<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_8' onclick='computeCode("+ $(this).data('id') +");'></td><!-- CodeMCount -->\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_7' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_6' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_5' onclick='computeCode("+ $(this).data('id') +");'></td><!-- UAL -->\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_4' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_3' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_2' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_1' onclick='computeCode("+ $(this).data('id') +");'></td>\
				<td><input type='checkbox' id='checkbox" + $(this).data('id') + "_0' onclick='computeCode("+ $(this).data('id') +");'></td>";

			html += "<td><input type='text' id='adr"+$(this).data('id')+"' size='2' maxLength='2' value='00' oninput='computeCode("+$(this).data('id')+");'></td> <!-- Adr -->\
				<td><input type='text' id='microcode"+$(this).data('id')+"' size='15' maxLength='10' value='0x00000000' onchange='fromCode("+$(this).data('id')+")'></td>";

			html += "</tr></tbody>";
			html += "</table>";

			html += "<input class=\"md-button\" type='button' value='Reinitialiser le code' onclick='initCode("+$(this).data('id')+");'/>";
			html += "</div></div>";
			$(this).html($(html));
		});

})
