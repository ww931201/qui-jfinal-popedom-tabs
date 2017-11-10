/*edited by fukai*/
var bw;
(function (jQuery){
	this.version = '@1.1';
	this.layer = {'width' : 200, 'height': 100};
	this.title = uncompile(quiLanguage.messager.title);
	this.time = 4000;
	this.anims = {'type' : 'slide', 'speed' : 600};
	
	this.inits = function(title, text){
		if($("#message").is("div")){ return; }
		var $box_msg=$('<div id="message" style="z-index:800;width:'+this.layer.width+'px;position:absolute; display:none;bottom:0px; right:1px; overflow:hidden;"></div>')
		var $box_msg_top=$('<div class="msg_topcenter"><div class="msg_topleft"><div class="msg_topright"><div class="msg_title">'+title+'</div><div class="msg_close" id="message_close"></div></div></div></div>')
		var $box_msg_middle=$('<div class="msg_middlecenter"><div class="msg_middleleft"><div class="msg_middleright"><div class="boxContent" style="height:'+(this.layer.height-35)+'px;">'+text+'</div></div></div></div>')
		var $box_msg_bottom=$('<div class="msg_bottomcenter"><div class="msg_bottomleft"><div class="msg_bottomright"></div></div></div>')
		$box_msg.append($box_msg_top).append($box_msg_middle).append($box_msg_bottom);
		$(document.body).append($box_msg);
	};
	this.show = function(title, text, time,sound,style){
		if($("#message").is("div")){ return; }
		if(title==0 || !title)title = this.title;
		this.inits(title, text);
		if(time){
			this.time = time;
		}
		if(style!=null&&style!=""){
			$("#message").addClass(style);
		}
		else{
			$("#message").addClass("box_msg");
		}
		switch(this.anims.type){
			case 'slide':$("#message").slideDown(this.anims.speed);break;
			case 'fade':$("#message").fadeIn(this.anims.speed);break;
			case 'show':$("#message").show(this.anims.speed);break;
			default:$("#message").slideDown(this.anims.speed);break;
		}
		$("#message_close").click(function(){		
			mesclose()
			
		});
		//$("#message").slideDown('slow');
		if(sound!=null&&sound!=""){
			try {
				 var $sound=$("<embed src="+sound+"  AutoStart='true' type='application/x-mplayer2'></embed>")
				 $("body").append($sound)
			}
			catch(e){}
		}
		this.rmmessage(this.time);
	};
	this.lays = function(width, height){
		if($("#message").is("div")){ return; }
		if(width!=0 && width)this.layer.width = width;
		if(height!=0 && height)this.layer.height = height;
	}
	this.anim = function(type,speed){
		if($("#message").is("div")){ return; }
		if(type!=0 && type)this.anims.type = type;
		if(speed!=0 && speed){
			switch(speed){
				case 'slow' : ;break;
				case 'fast' : this.anims.speed = 200; break;
				case 'normal' : this.anims.speed = 400; break;
				default:					
					this.anims.speed = speed;
			}			
		}
	}
	this.rmmessage = function(time){
		if(time!='stay'){
			bw=setTimeout('mesclose()', time);
		}
	};
	this.mesclose = function(){
		clearTimeout(bw)
		switch(this.anims.type){
			case 'slide':$("#message").slideUp(this.anims.speed);break;
			case 'fade':$("#message").fadeOut(this.anims.speed);break;
			case 'show':$("#message").hide(this.anims.speed);break;
			default:$("#message").slideUp(this.anims.speed);break;
		};
	
		var msgTimeOut=setTimeout('$("#message").remove();', this.anims.speed);
		setTimeout('$("#messageSound").remove();', this.anims.speed);
		this.original();
	};
	this.original = function(){	
		this.layer = {'width' : 200, 'height': 100};
		this.title = uncompile(quiLanguage.messager.title);
		this.time = 4000;
		this.anims = {'type' : 'slide', 'speed' : 600};
	};
    jQuery.messager = this;
    return jQuery;
})(jQuery);