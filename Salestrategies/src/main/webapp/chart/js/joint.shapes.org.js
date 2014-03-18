/*! JointJS v0.8.0 - JavaScript diagramming library  2014-01-22


 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this
 file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
if (typeof exports === 'object') {

    var joint = {
    util: require('../src/core').util,
    shapes: {},
    dia: {
    Element: require('../src/joint.dia.element').Element,
    Link: require('../src/joint.dia.link').Link
    }
    };
}

joint.shapes.org = {};

joint.shapes.org.Member = joint.dia.Element.extend({

                                                   markup: '<g class="rotatable"><g class="scalable"><rect class="card"/><image/></g><text class="rank"/><text class="name"/></g>',

                                                   defaults: joint.util.deepSupplement({

                                                                                       type: 'org.Member',
                                                                                       size: { width: 150, height: 70 },
                                                                                       attrs: {

                                                                                       rect: { width: 218, height: 80 },

                                                                                       '.card': {
                                                                                       fill: '#FFFFFF', stroke: '#000000', 'stroke-width': 2,
                                                                                       'pointer-events': 'visiblePainted', rx: 10, ry: 10
                                                                                       },

                                                                                       image: {
                                                                                       width: 48, height: 48,
                                                                                       ref: '.card', 'ref-x': 10, 'ref-y': 5
                                                                                       },

                                                                                       '.rank': {
                                                                                       'text-decoration': 'underline',
                                                                                       ref: '.card', 'ref-x': 0.9, 'ref-y': 0.1,
                                                                                       'font-family': 'Courier New', 'font-size': 14,
                                                                                       'text-anchor': 'end'
                                                                                       },

                                                                                       '.name': {
                                                                                       'font-weight': 'bold',
                                                                                       ref: '.card', 'ref-x': 0.9, 'ref-y': 0.4,
                                                                                       'font-family': 'Courier New', 'font-size': 14,
                                                                                       'text-anchor': 'end'
                                                                                       }
                                                                                       }
                                                                                       }, joint.dia.Element.prototype.defaults)
                                                   });

joint.shapes.org.MemberView = joint.dia.ElementView.extend({

                                                           template: [
                                                                      '<div class="html-element">',
                                                                      '<label></label>',
                                                                      '<div class="sp1"></div><select class="sel1"><option>V</option><option>R</option><option>F</option><option>P</option><option>T</option></select>',
                                                                      '<div class="sp2"></div><select class="sel2"><option>A</option><option>C</option><option>D</option><option>E</option><option>U</option></select>',
                                                                      '<div class="sp3"></div><select class="sel3"><option>★</option><option>+</option><option>=</option><option>-</option><option>X</option></select>',
                                                                      '<div class="sp4"></div><select class="sel4"><option>0</option><option>1</option><option>2</option><option>3</option></select>',
                                                                      '</div>'
                                                                      ].join(''),

                                                           initialize: function() {
                                                           _.bindAll(this, 'updateBox');
                                                           joint.dia.ElementView.prototype.initialize.apply(this, arguments);

                                                           this.$box = $(_.template(this.template)());
                                                           // Prevent paper from handling pointerdown.
                                                           this.$box.find('input,select').on('mousedown click', function(evt) { evt.stopPropagation(); });

                                                           // This is an example of reacting on the input change and storing the input data in the cell model.
                                                           this.$box.find('input').on('change', _.bind(function(evt) {
                                                                                                       this.model.set('input', $(evt.target).val());
                                                                                                       }, this));
                                                           this.$box.find('select').on('change', _.bind(function(evt) {
                                                                                                        this.model.set('select', $(evt.target).val());
                                                                                                        $(evt.target).prev().text($(evt.target).val());
                                                                                                        $(evt.target).prev().css({"display":"block"});
                                                                                                        this.$box.find('select').css({"display":"none"});
																										//todo
                                                                                                        }, this));
                                                           this.$box.find('select').val(this.model.get('select'));
                                                           // Update the box position whenever the underlying model changes.
                                                           this.model.on('change', this.updateBox, this);
                                                           this.initBox();
                                                           //init id
                                                           this.$box.find('label').attr('id',this.$box.find('label').text());
                                                           this.$box.find('.sp1').text(this.model.get('sp1'));
                                                           this.$box.find('.sel1').val(this.model.get('sp1'));
                                                           this.$box.find('.sp2').text(this.model.get('sp2'));
                                                           this.$box.find('.sel2').val(this.model.get('sp2'));
                                                           this.$box.find('.sp3').text(this.model.get('sp3'));
                                                           this.$box.find('.sel3').val(this.model.get('sp3'));
                                                           this.$box.find('.sp4').text(this.model.get('sp4'));
                                                           this.$box.find('.sel4').val(this.model.get('sp4'));
                                                           },
                                                           render: function() {
                                                           joint.dia.ElementView.prototype.render.apply(this, arguments);
                                                           this.paper.$el.prepend(this.$box);
                                                           //this.updateBox();
                                                           return this;
                                                           },
                                                           updateBox: function() {
                                                           // Set the position and dimension of the box so that it covers the JointJS element.
                                                           var bbox = this.model.getBBox();
                                                           this.$box.find('label').text(this.model.get('label'));
                                                           // Example of updating the HTML with a data stored in the cell model.
                                                           //alert(this.model.get('select'));
                                                           this.$box.css({ width: bbox.width, height: bbox.height, left: bbox.x, top: bbox.y });
                                                           },
                                                           initBox: function() {
                                                           // Set the position and dimension of the box so that it covers the JointJS element.
                                                           var bbox = this.model.getBBox();
                                                           this.$box.find('label').text(this.model.get('label'));
                                                           // Example of updating the HTML with a data stored in the cell model.
//                                                           alert(this.model.get('select'));
                                                           this.$box.css({ width: bbox.width, height: bbox.height, left: bbox.x, top: bbox.y });
                                                           },
                                                           pointerdown: function () {
                                                           this._click = true;
                                                           joint.dia.ElementView.prototype.pointerdown.apply(this, arguments);
                                                           },
                                                           pointermove: function () {
                                                           this._click = false;
                                                           joint.dia.ElementView.prototype.pointermove.apply(this, arguments);
                                                           },
                                                           pointerup: function (evt, x, y) {
                                                           if (this._click) {
                                                           // triggers an event on the paper and the element itself
                                                           this.notify('cell:click', evt, x, y);
                                                           var bbox = this.model.getBBox();
                                                           console.log('click! x='+ (x-bbox.x) + ' y=' + (y-bbox.y));
                                                           this.$box.find('select').css({"display":"none"});
                                                           this.$box.find('div').css({"display":"block"});
                                                           if((x-bbox.x)>115 && (x-bbox.x)<145 && (y-bbox.y)>55 && (y-bbox.y)<68){

                                                           this.$box.find('div.sp1').css({"display":"none"});
                                                           this.$box.find('select.sel1').css({"display":"block"});
                                                           }else
                                                           if((x-bbox.x)>93 && (x-bbox.x)<115 && (y-bbox.y)>56 && (y-bbox.y)<68){
                                                           this.$box.find('div.sp2').css({"display":"none"});
                                                           this.$box.find('select.sel2').css({"display":"block"});
                                                           }else
                                                           if((x-bbox.x)>68 && (x-bbox.x)<85 && (y-bbox.y)>55 && (y-bbox.y)<68){
                                                           this.$box.find('div.sp3').css({"display":"none"});
                                                           this.$box.find('select.sel3').css({"display":"block"});
                                                           }else
                                                           if((x-bbox.x)>40 && (x-bbox.x)<61 && (y-bbox.y)>58 && (y-bbox.y)<68){
                                                           this.$box.find('div.sp4').css({"display":"none"});
                                                           this.$box.find('select.sel4').css({"display":"block"});
                                                           }
                                                           } else {
                                                           joint.dia.ElementView.prototype.pointerup.apply(this, arguments);
                                                           }
                                                           }
                                                           });



joint.shapes.org.Arrow = joint.dia.Link.extend({
                                               
                                               defaults: {
                                               type: 'org.Arrow',
                                               source: { selector: '.card' }, target: { selector: '.card' },
                                               attrs: { '.connection': { stroke: '#585858', 'stroke-width': 3 }},
                                               z: -1
                                               }
                                               });


if (typeof exports === 'object') {
    
    module.exports = joint.shapes.org;
}
