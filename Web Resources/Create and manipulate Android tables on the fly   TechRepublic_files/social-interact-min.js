CNB.SocialMedia=new Class({Implements:[Options,Events],options:{title:'',url:'',shortUrl:'',summary:'',container:null,twitterRelated:'techrepublic,zdnet',cmntCount:0,favHost:'',contentType:'Blog posts',contentId:'',cmntSuffix:{singular:'Comment',plural:'Comments'},onHackernewsSubmit:function(){window.open('http://news.ycombinator.com/submitlink?u='+this.url+'&t='+this.encodedTitle+'&summary='+this.summary);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;hacker-news;click'})},onDiggSubmit:function(){window.open('http://digg.com/submit?url='+this.encodedUrl+'&title='+this.encodedTitle);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;digg;click',usrAction:152})},onEmailSubmit:function(){window.open('mailto:?subject='+this.encodedTitle+'&body='+this.encodedUrl+' %0D%0D'+this.encodedSummary);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;email;click',usrAction:19})},onFacebookSubmit:function(){var c=this.getPopUpCoords(626,436);window.open('http://www.facebook.com/sharer.php?u='+this.encodedUrl+'&t='+this.encodedTitle,'facebook_share','width='+c.w+',height='+c.h+',left='+c.x+',top='+c.y+',toolbar=0,personalbar=0,status=0,resizable=1');DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;facebook;click',usrAction:501})},onRedditSubmit:function(){window.open('http://reddit.com/submit?url='+this.encodedUrl+'&title='+this.encodedTitle);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;reddit;click'})},onStumbleuponSubmit:function(){window.open('http://www.stumbleupon.com/submit?url='+this.encodedUrl);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;stumbleupon;click'})},onTwitterSubmit:function(){var c=this.getPopUpCoords(550,450);var twitterUrl='http://twitter.com/share?text='+this.encodedTitle;twitterUrl+='&related='+encodeURIComponent(this.options.twitterRelated);twitterUrl+=(!this.shortUrl)?'&url='+this.url:'&url='+this.shortUrl+'&counturl='+this.encodedUrl;window.open(twitterUrl,'twitter_tweet','width='+c.w+',height='+c.h+',left='+c.x+',top='+c.y+',personalbar=0,toolbar=0,scrollbars=1,resizable=1');DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;twitter;click',usrAction:460,tag:'twitter'})},onDeliciousSubmit:function(){window.open('http://del.icio.us/post?url='+this.encodedUrl+'&title='+this.encodedTitle);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;delicious;click'})},onGooglebuzzSubmit:function(){window.open('http://www.google.com/buzz/post?url='+this.encodedUrl);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;google-buzz;click'})},onNewsvineSubmit:function(){window.open('http://www.newsvine.com/_tools/seed&amp;save?url='+this.encodedUrl+'&title='+this.encodedTitle);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;newsvine;click'})},onTechnoratiSubmit:function(){window.open('http://technorati.com/faves?add='+this.encodedUrl);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;technorati;click'})},onLinkedinSubmit:function(){window.open('http://www.linkedin.com/shareArticle?mini=true&url='+this.encodedUrl+'&title='+this.encodedTitle+'&summary='+this.encodedSummary);DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;linked-in;click'})},onPrintSubmit:function(){window.print();DW.redir({ctype:'shareapp;type;evt',cval:'sharebar;print;click'})},onTrfavoriteSubmit:function(e,el){var host=(this.options.favHost)?this.options.favHost:'http://www.techrepublic.com/util/favorite';var props={title:this.title,url:this.url,contentType:this.options.contentType,contentId:this.options.contentId,path:this.url};var url=host+'?'+$H(props).toQueryString();el.set('href',url);new CNB.TR.Favorite(e,el).toggle()}},initialize:function(options){this.setOptions(options);this.url=this.options.url;this.shortUrl=this.options.shortUrl;this.title=this.options.title;this.summary=this.options.summary;this.encodedUrl=encodeURIComponent(this.options.url);this.encodedShortUrl=encodeURIComponent(this.options.shortUrl);this.encodedTitle=encodeURIComponent(this.options.title);this.encodedSummary=encodeURIComponent(this.options.summary);this.cmntCount=this.options.cmntCount;this.cmntSuffix=(this.options.cmntCount===1)?this.options.cmntSuffix.singular:this.options.cmntSuffix.plural;this.linkOptions={'email':'Email','print':'Print','trfavorite':'Add to Favorites','delicious':'Del.icio.us','digg':'Digg','facebook':'Facebook','googlebuzz':'Google Buzz','hackernews':'Hacker News','linkedin':'LinkedIn','reddit':'Reddit','stumbleupon':'StumbleUpon','technorati':'Technorati','twitter':'Twitter','newsvine':'Newsvine'}},load:function(){this.fireEvent('load');if(this.options.container){this.applyActions(this.options.container)}this.fireEvent('loadEnd')},applyActions:function(containers){$splat(containers).each(function(container){container.addEvent('click:relay([shareaction])',this.handleShareAction.bind(this))},this)},handleShareAction:function(e,el){e.stop();var action=el.getProperty('shareaction');this.fireEvent(action+'Submit',[e,el])},getPopUpCoords:function(w,h){var sw=screen.width,sh=screen.height;var x=Math.round((sw/2)-(w/2));var y=(sh>h)?Math.round((sh/2)-(h/2)):0;return{x:x,y:y,w:w,h:h}},getButtonHtml:function(name,mode){var fn='get'+name.capitalize()+'Button';if($type(this[fn])=='function'){return this[fn](mode)}else{return false}},getLinkData:function(name){if(name in this.linkOptions){return this.linkOptions[name]}else{return false}},getFacebookButton:function(mode){mode=mode||'vertical';var height='60';var width='50';var layout='box_count';switch(mode){case'horizontal':height='21';width='85';layout='button_count';break;case'horizontal-wide':height='24';width='350';layout='standard';break}return'<iframe src="http://www.facebook.com/plugins/like.php?href='+this.encodedUrl+'&amp;send=false&amp;layout='+layout+'&amp;width='+width+'&amp;height='+height+'&amp;show_faces=false&amp;action=like&amp;colorscheme=light&amp;font=arial" '+'scrolling="no" frameborder="0" style="border:none;overflow:hidden;width:'+width+'px;height:'+height+'px;" allowTransparency="true"></iframe>'},getTwitterButton:function(mode){mode=mode||'vertical';var height='62';var width='55';if(mode=='horizontal'){width='102';height='20'}var props={count:mode,related:this.options.twitterRelated,text:this.title};if(this.shortUrl){props.url=this.shortUrl;props.counturl=this.url}else{props.url=this.url}var data='<iframe allowtransparency="true" frameborder="0" scrolling="no"'+'src="http://platform.twitter.com/widgets/tweet_button.html?'+$H(props).toQueryString()+'"'+'style="width:'+width+'px;height:'+height+'px;border:none;overflow:hidden;"></iframe>';return data},getTwitterfollowButton:function(mode,screenName){if(!screenName){return false}mode=mode||'horizontal';var showScreenName=(mode=='horizontal')?'true':'false';var showCount=(mode=='horizontal')?'false':'true';var linkColor='';var textColor='';var height='20';var width='300';var props={screen_name:screenName,show_count:showCount,button:'blue',text_color:textColor,link_color:linkColor,lang:'en',show_screen_name:showScreenName};var data='<iframe class="twitter-follow-button" allowtransparency="true" frameborder="0" scrolling="no"'+'src="http://platform0.twitter.com/widgets/follow_button.html?'+$H(props).toQueryString()+'"'+'style="width:'+width+'px;height:'+height+'px;border:none;overflow:hidden;"></iframe>';return data},getStumbleuponButton:function(mode){mode=mode||'vertical';var height='60';var width='50';var type='5';if(mode=='horizontal'){height='18';width='74';type='1'}return'<iframe src="http://www.stumbleupon.com/badge/embed/'+type+'/?url='+this.encodedUrl+'" scrolling="no" frameborder="0" style="border:none;overflow:hidden;width:'+width+'px;height:'+height+'px;" allowTransparency="true"></iframe>'},getGoogleplusButton:function(mode){mode=mode||'vertical';this.addScript('https://apis.google.com/js/plusone.js');var type=(mode=='horizontal')?'medium':'tall';var data='<div class="g-plusone" data-size="'+type+'" data-href="'+this.url+'" data-count="true"></div>';return data},addScript:function(src){this.addEvent('loadEnd',function(){var s=document.createElement('script');s.async=true;s.type='text/javascript';s.src=src;(document.getElementsByTagName('head')[0]||document.getElementsByTagName('body')[0]).appendChild(s)})}});CNB.SocialInteractUnit=new Class({Extends:CNB.SocialMedia,options:{modeOverride:null,contentContainer:null,showFocusHighlight:false,buttons:['twitter','facebook','stumbleupon','googleplus'],links:['email','print','delicious','digg','linkedin','reddit','technorati'],showTalkback:true,windowInset:{top:100,bottom:0}},initialize:function(contHorz,options){this.parent(options);this.showTalkback=this.options.showTalkback;this.contHorz=$(contHorz);this.activeModes=[];this.buttons=this.options.buttons;this.links=this.options.links;if(this.options.modeOverride!='vertical'){this.activeModes.push('horizontal')}if(this.options.contentContainer&&this.options.modeOverride!='horizontal'){this.content=$(this.options.contentContainer);this.content.setStyle('position','relative');this.content.addClass('clear');this.contVert=new Element('div',{'class':'siu-vertical-cont'}).inject(this.content,'top');this.activeModes.push('vertical');var xElPos=this.contVert.getPosition(this.content).x;var elGutterWidth=(xElPos>=0)?xElPos:-(xElPos);this.minGutterSize=elGutterWidth+10;window.addEvent('resize',this.handleResize.bind(this))}this.addEvents({'load':this.handleLoad.bind(this),'modeChange':this.handleModeChange.bind(this)})},loadHtml:function(){this.activeModes.each(function(mode){var cont=this.getModeContainer(mode);var html=this.getHtml(mode);cont.set('html',html);cont.store('isLoaded',true)},this)},getModeContainer:function(mode){return(mode=='vertical')?this.contVert:this.contHorz},handleResize:function(){var mode=this.detectMode();if(mode!=this.mode){this.mode=mode;this.fireEvent('modeChange')}},handleLoad:function(){this.loadHtml();this.mode=this.detectMode();this.activeModes.each(function(mode){var cont=this.getModeContainer(mode);var options;switch(mode){case'vertical':options={position:{x:'right',y:'bottom'},offset:{y:0,x:0},edge:{x:'left',y:'bottom'}};new CNB.Fixated(cont,{windowInset:this.options.windowInset,addPlaceholder:false,container:this.content});break;case'horizontal':options={position:{x:'right',y:'bottom'},offset:{y:0,x:0},edge:{x:'right',y:'top'}};break}new CNB.Pop(cont.getElement('.siu-more-btn'),cont.getElement('.siu-more'),options).load();if(this.options.showFocusHighlight){new CNB.FocusHighlight(cont)}this.applyActions(cont)},this);this.setDisplay(this.mode)},handleModeChange:function(){this.mode=this.detectMode();this.setDisplay(this.mode)},setDisplay:function(mode){if(!this.activeModes.contains(mode)){return}this.activeModes.each(function(activeMode){var cont=this.getModeContainer(activeMode);if(cont){if(activeMode==mode){cont.setStyle('display','block')}else{cont.setStyle('display','none')}}},this)},detectMode:function(){if(this.options.modeOverride){return this.options.modeOverride}var contentPos=this.content.getPosition();return(contentPos.x<this.minGutterSize)?'horizontal':'vertical'},getHtml:function(mode){var o='<div class="siu siu-'+mode+' clear">';if(this.showTalkback){o+='<div class="grp siu-cmnt-cont"><a class="siu-cmnt" href="#talkback"><span class="count">'+this.cmntCount+'</span><span class="suffix">'+this.cmntSuffix+'</span></a></div>'}if(this.buttons){o+='<div class="grp clear siu-btns-cont">';this.buttons.each(function(name){var html=this.getButtonHtml(name,mode);if(html){o+='<div class="siu-btn-cont siu-'+name+'-btn">'+html+'</div>'}},this);o+='</div>'}if(this.links){o+='<div class="grp siu-more-cont"><a class="siu-more-btn">more +</a>';o+='<div class="siu-more"><ul class="options clear">';this.links.each(function(name){var data=this.getLinkData(name);if(data){o+='<li><a class="icon i-'+name+'" shareaction="'+name+'">'+data+'</a></li>'}},this);o+='</ul></div></div>'}o+='</div>';return o}});CNB.Voting=new Class({Implements:[Options,Events],options:{url:null,container:null,loadingContainer:null,btn:null,regSrc:'vote',data:{cid:null,title:null,url:null,ct:null,siteId:null,assetTypeId:null,pollId:null,answerId:null},hasVoted:false,onSuccess:function(data){var suffix=(data.voteCount==1)?'Vote':'Votes';var voteCount=(data.voteCount>0)?'+'+data.voteCount:data.voteCount;this.container.getElement('.count').set('text',voteCount);this.container.getElement('.suffix').set('text',suffix)},},initialize:function(options){this.setOptions(options);this.voteHistory=Cookie.read('service');this.data=this.options.data;this.url=this.options.url;this.container=$(this.options.container);this.loadingContainer=$(this.options.loadingContainer);this.btn=this.options.btn},activate:function(){this.container.addEvent('click:relay(a[voteaction])',this.handleClickEvent.bind(this))},handleClickEvent:function(e,el){e.stop();this.btn=el;this.attempt()},attempt:function(){if(!CNB.hasCookiesEnabled()){new CNB.Overlay().notify('You must enable cookies to vote');return}if(!this.container){this.container=this.btn}if(this.container.retrieve('voted')===true||this.btn.hasClass('disabled')){return false}if(this.loadingContainer){this.loader=new CNB.Loading(this.loadingContainer)}this.fireEvent('click',this.btn);CNB.Reg.gatedEvent(null,this.handleVoteInit.bind(this),this.options.regSrc,'Please Log In To Cast Your Vote.')},handleVoteInit:function(){var action=this.btn.getProperty('voteaction'),data=JSON.decode(this.btn.getProperty('data-voting')),credit=null;switch(action){case'vote-down':credit='-1';break;case'vote-up':default:credit='1'}this.data=$merge(this.data,data,{'cr':credit});this.makeVoteRequest()},makeVoteRequest:function(){if(this.loader){this.loader.add()}this.container.store('voted',true);this.fireEvent('startRequest');var request=new Request.JSON({url:this.url,data:this.data,onSuccess:this.handleVoteResponse.bind(this),onComplete:function(){if(this.loader){this.loader.remove()}this.fireEvent('complete')}.bind(this)}).send()},handleVoteResponse:function(data){if(!data||typeof data.status=='undefined'){return false}if(data.status=='success'){this.fireEvent('success',data);this.disableBtns();DW.redir({usrAction:421})}else{if(typeof data.gate!='undefined'){switch(data.gate){case'login':CNB.Reg.gatedEvent(null,this.handleVoteInit.bind(this),this.options.regSrc,'Please Log In To Cast Your Vote.');break;case'confirm':CNB.Reg.confirmAccount();break}}}},disableBtns:function(){var btns=this.container.getElements('a[voteaction]');if(btns){btns.each(function(btn){btn.addClass('disabled')})}}});