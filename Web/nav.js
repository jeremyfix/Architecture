
var scripts = document.getElementsByTagName('script');
var lastScript = scripts[scripts.length-1];
var scriptName = lastScript;
whichactive = scriptName.getAttribute('active');

var mystr = "    <nav class='navbar navbar-inverse navbar-fixed-top'>"+
    "      <div class='container-fluid'>"+
    "        <div class='navbar-header'>"+
    "          <button type='button' class='navbar-toggle collapsed' data-toggle='collapse' data-target='#navbar' aria-expanded='false' aria-controls='navbar'>"+
    "            <span class='sr-only'>Toggle navigation</span>"+
    "            <span class='icon-bar'></span>"+
    "            <span class='icon-bar></span>"+
    "            <span class='icon-bar'></span>"+
    "          </button>"+
    "          <!--<a class='navbar-brand' href='#'>Architecture des ordinateurs</a>-->"+
    "        </div>"+
    "        <div id='navbar' class='collapse navbar-collapse'>"+
    "          <ul class='nav navbar-nav'>";

var links = ["<a href='index.html'>Page principale</a>", 
	     "<a href='seq_man.html'>Séquenceur manuel (BE) 1/5</a>", 
	     "<a href='seq.html'>Séquenceur microprogrammé (TL) 2/5</a>",
	     "<a href='routines.html'>Pile et appel de routines (BE) 3/5 </a>",
	     "<a href='irq.html'>Interruptions (BE) 4/5</a>",
	     "<a href='ordonnanceur.html'>Programmation assembleur et ordonnancement (TL) 5/5</a>",
	     "<a href='codes.html'>Générateurs de micro-code</a>",
	     "<a href='jeu.html'>Bonus : Place aux jeux</a>"
	    ];
	     // "<a href='bonus.html'>Bonus 6/5</a>"]; 

for (i = 0; i < links.length; i++) {
    if(i == whichactive) {
	mystr += "<li class='active'>" + links[i] + "</li>";
    }
    else {
	mystr += "<li>" + links[i] + "</li>";
    }
}

mystr += "	    <li></li>"+
    "	    <li></li>"+
    "           <li></li>"+
    "           <li></li>"+
    "           <li></li>"+
    "          </ul>"+
    "        </div><!--/.nav-collapse -->"+
    "      </div>"+
    "    </nav>";

document.write(mystr);
