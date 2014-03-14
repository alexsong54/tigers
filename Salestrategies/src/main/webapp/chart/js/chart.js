var graph = new joint.dia.Graph;

var paper = new joint.dia.Paper({
                                el: $('#paper'),
                                width: 5000,
                                height: 800,
                                gridSize: 1,
                                model: graph,
                                perpendicularLinks: true,
                                elementView: joint.shapes.org.MemberView
                                });

var member = function(x, y, rank, name, image, id, border) {

    var background;
    if(rank=='CEO'){
        y = 70;
        x=x1;
        x1+=180;
        background = '#F1C40F';
    }else if(rank.slice(0,2)=='VP'){
        y = 200;
        x=x2;
        x2+=210;
        background = '#2ECC71';
    }else if(rank.slice(0,2)=='Ma'){
        y = 330;
        x=x3;
        x3+=210;
        background = '#3498DB';
    }else{
        y = 460;
        x=x4;
        x4+=210;
        background = '#3EF3A2';
    }
    var cell = new joint.shapes.org.Member({
                                           position: { x: x, y: y },
                                           attrs: {
                                           '.card': { fill: background, stroke: border},
                                           image: { 'xlink:href': '../chart/images/'+ image },
                                           '.rank': { text: rank }, '.name': { text: name }
                                           }, label: id
                                           });
    graph.addCell(cell);
    return cell;
};

function link(source, target, breakpoints) {

    var cell = new joint.shapes.org.Arrow({
                                          source: { id: source.id },
                                          target: { id: target.id },
                                          vertices: breakpoints
                                          });

    cell.attr({
              '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }
              });
    graph.addCell(cell);
    return cell;
}

function link2(source, target, breakpoints) {

    var cell = new joint.shapes.org.Arrow({
                                          source: { id: source.id },
                                          target: { id: target.id },
                                          vertices: breakpoints
                                          });
    cell.set('smooth', true);
    cell.attr({
              '.connection': { stroke: 'red' },
              '.marker-source': { fill: 'red', d: 'M 10 0 L 0 5 L 10 10 z' },
              '.marker-target': { fill: 'red', d: 'M 10 0 L 0 5 L 10 10 z' }
              });
    graph.addCell(cell);
    return cell;
}

paper.on('cell:pointerup', function(cellView) {

         // We don't want a Halo for links.
         if (cellView.model instanceof joint.dia.Link) return;

          console.log('move');
         });

var x1=300;
var x2=10;
var x3=10;
var x4=10;

function checkId(id1,id2,listofmember){
    var index1,index2;
    for(i2=0;i2<listofmember.length;i2++){
        if(listofmember[i2].id==id1){
            index1=i2;
        }
        if(listofmember[i2].id==id2){
            index2=i2;
        }
    }
    if(index1>index2){
        return true;
    }
    return false;
}

function sortMember(listofmember) {
    var key;
    var t;
    for (j=1;j<listofmember.length;j++)
    {
        key=listofmember[j];
        t=j-1;
        while (t>=0 && listofmember[t].ranklevel>key.ranklevel)
        {
            listofmember[t+1]=listofmember[t];
            listofmember[t]=key;
            t--;
        }
    }
    var rankcount=[0,0,0,0,0,0];
    for (j=0;j<listofmember.length;j++){
        rankcount[listofmember[j].ranklevel]++;
    }
    var cindex=0;
    for (j=0;j<rankcount.length;j++){
        if (rankcount[j]>1){
            for (i=cindex+1;i<cindex+rankcount[j];i++)
            {
                key=listofmember[i];
                t=i-1;
                while (t>=0 && checkId(listofmember[t].report_to,key.report_to,listofmember))
                {
                    listofmember[t+1]=listofmember[t];
                    listofmember[t]=key;
                    t--;
                }
            }
        }
        cindex=cindex+rankcount[j];
    }
}


//$(".html-element span")
//.unbind("click")
//.click(function()
//       {
//       log('aaaaaaaaa');
//       $(".html-element span").hide();
//       this.next()
//       .each(function(i,v)
//             {
//             $(this)
//             .css({
//                  "display":"block",
//                  "backgound":"none",
//                  "border-bottom":"1px solid #ccc"
//                  });
//             });
//       });
var tdataString = $("#data").attr("value");
var lom = eval(tdataString);
/*lom[0]=new Object();
lom[0].name='Bart Simpson';
lom[0].rank='CEO';
lom[0].ranklevel=0;
lom[0].report_to=0;
lom[0].id=10;

lom[1]=new Object();
lom[1].name='Mark Simpson';
lom[1].rank='Manager';
lom[1].ranklevel=2;
lom[1].report_to=16;
lom[1].id=11;

lom[2]=new Object();
lom[2].name='Elli Simpson';
lom[2].rank='VP Marketing';
lom[2].ranklevel=1;
lom[2].report_to=10;
lom[2].id=12;

lom[3]=new Object();
lom[3].name='Azoi Keop';
lom[3].rank='Manager';
lom[3].ranklevel=2;
lom[3].report_to=20;
lom[3].id=13;

lom[4]=new Object();
lom[4].name='Xyman Daras';
lom[4].rank='Sales';
lom[4].ranklevel=3;
lom[4].report_to=11;
lom[4].id=14;

lom[5]=new Object();
lom[5].name='Keto Lazerd';
lom[5].rank='Manager';
lom[5].ranklevel=2;
lom[5].report_to=16;
lom[5].id=15;

lom[6]=new Object();
lom[6].name='Momento Mori';
lom[6].rank='VP Sales';
lom[6].ranklevel=1;
lom[6].report_to=10;
lom[6].id=16;

lom[7]=new Object();
lom[7].name='Trans Am';
lom[7].rank='Sales';
lom[7].ranklevel=3;
lom[7].report_to=15;
lom[7].id=17;

lom[8]=new Object();
lom[8].name='Exia Dole';
lom[8].rank='Sales';
lom[8].ranklevel=3;
lom[8].report_to=11;
lom[8].id=18;

lom[9]=new Object();
lom[9].name='Gunda Plae';
lom[9].rank='Manager';
lom[9].ranklevel=2;
lom[9].report_to=12;
lom[9].id=19;

lom[10]=new Object();
lom[10].name='Amuro Rei';
lom[10].rank='VP Production';
lom[10].ranklevel=1;
lom[10].report_to=10;
lom[10].id=20;

lom[11]=new Object();
lom[11].name='Char Azunaburu';
lom[11].rank='Sales';
lom[11].ranklevel=3;

lom[11].report_to=15;
lom[11].id=21;
//红边
lom[11].core=21;
//红线
lom[11].influence_to=10;

lom[11].gender='F';*/

//TODO: ajax here to load objects
sortMember(lom);
//alert(lom);

var rendered=new Array();
var lnk=new Array();
var lnkbk=new Array();
var lnk2=new Array();
var lnk2bk=new Array();

for(i3=0;i3<lom.length;i3++){
    rendered[i3]=member(1,1,lom[i3].rank,lom[i3].name,lom[i3].gender != '2'?'member1.png':'member2.png', lom[i3].id, lom[i3].core != null?'red':'gray');
}

for(i3=0;i3<lom.length;i3++){
    if(lom[i3].report_to!=0){
        for(i4=0;i4<lom.length;i4++){
            if(lom[i4].id==lom[i3].report_to){
                lnk[i3]=link(rendered[i3],rendered[i4]);
                lnk[i3].on('change:target', function() {
                           if(this.get('target').id!=null){
                           console.log('ID:'+this.get('target').id);
                           for(i5=0;i5<lom.length;i5++){
                           if(rendered[i5].id==this.get('target').id)
                           console.log(lom[i5].id);
                           //TODO: ajax here to update this object
                           break;
                           }
                           }
                           });
                lnkbk[i3]=new Object();
                lnkbk[i3].id=lnk[i3].id;
                lnkbk[i3].source=lnk[i3].get('source').id;
                lnk[i3].on('change:source', function() {
                           for(i5=0;i5<lom.length;i5++){
                           if(lnkbk[i5]!=null && this.id==lnkbk[i5].id){
                           this.set('source', { id: lnkbk[i5].source });
                           console.log(lom[i5].id);
                           break;
                           }
                           }
                           });
            }
        }
    }
}

for(i3=0;i3<lom.length;i3++){
    if(typeof(lom[i3].influence_toluence_to) !== 'undefined' && lom[i3].influence_to !== null && lom[i3].influence_to!=0){
        for(i4=0;i4<lom.length;i4++){
            if(lom[i4].id==lom[i3].influence_to){
                lnk2[i3]=link2(rendered[i3],rendered[i4]);
                lnk2[i3].on('change:target', function() {
                           if(this.get('target').id!=null){
                           console.log('ID:'+this.get('target').id);
                           for(i5=0;i5<lom.length;i5++){
                           if(rendered[i5].id==this.get('target').id)
                           console.log(lom[i5].id);
                           //TODO: ajax here to update this object
                           break;
                           }
                           }
                           });
                lnk2bk[i3]=new Object();
                lnk2bk[i3].id=lnk2[i3].id;
                lnk2bk[i3].source=lnk2[i3].get('source').id;
                lnk2[i3].on('change:source', function() {
                           for(i5=0;i5<lom.length;i5++){
                           if(lnk2bk[i5]!=null && this.id==lnk2bk[i5].id){
                           this.set('source', { id: lnk2bk[i5].source });
                           console.log(lom[i5].id);
                           break;
                           }
                           }
                           });

            }
        }
    }
}

$('#paper').width($(window).width()/12*9-150);

//var bart = member(450,70,'CEO', 'Bart Simpson', 'member1.png', '#F1C40F', 'gray');
//var homer = member(135,200,'VP Marketing', 'Homer Simpson', 'member2.png', '#2ECC71', '#008e09');
//var marge = member(450,200,'VP Sales', 'Marge Simpson', 'member3.png', '#2ECC71', '#008e09');
//var lisa = member(750,200,'VP Production' , 'Lisa Simpson', 'member4.png', '#2ECC71', '#008e09');
//var lenny = member(285,350,'Manager', 'Lenny Leonard', 'member6.png', '#3498DB', '#333');
//var carl = member(285,500,'Manager', 'Carl Carlson', 'member7.png', '#3498DB', '#333');
//var maggie = member(600,350,'Manager', 'Maggie Simpson', 'member5.png', '#3498DB', '#333');
//var mike = member(800,500,'Manager', 'Mike Lee', 'member7.png', '#3498DB', '#333');

//link(bart, marge);
//link(bart, homer);
//link(bart, lisa);
//link(homer, lenny);
//link(homer, carl);
//link(marge, maggie);
