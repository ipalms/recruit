import{d as e,r as s,R as t,bf as i,v as n,az as o,o as c,c as a,a as l,a6 as r,n as d}from"./index.ea109c08.js";var u=e({setup(){const e=n(),o=s(1),c=t.get("userId");function a(e,s){let t=!1;return e.forEach((e=>{e===s&&(t=!0)})),t}return{handleItemClick:function(e,s){o.value=e;const t=document.querySelectorAll(".course-introduce .swiper .item"),i=["left","right","middle","behind"],n=s.target.previousElementSibling||t[t.length-1],c=s.target.nextElementSibling||t[0],l=c.nextElementSibling||t[0]||n.previousElementSibling||t[t.length-1];i.forEach((e=>{a(s.target.classList,e)&&s.target.classList.remove(e)})),s.target.classList.add("middle"),i.forEach((e=>{a(n.classList,e)&&n.classList.remove(e)})),n.classList.add("left"),i.forEach((e=>{a(c.classList,e)&&c.classList.remove(e)})),c.classList.add("right"),i.forEach((e=>{a(l.classList,e)&&l.classList.remove(e)})),l.classList.add("behind")},currentCourse:o,handleConfirm:async function(){200===(await i(c,o.value)).error_code&&e.replace("/home")}}}});const m={class:"course-introduce"},v={class:"swiper"},h={key:0,class:"introduce"},f=l("div",{class:"title"},"前 端",-1),C=l("div",{class:"content"}," 前端即网站前台部分，运行在PC端，移动端等浏览器上展现给用户浏览的网页。随着互联网技术的发展，HTML5，CSS3，前端框架的应用，跨平台响应式网页设计能够适应各种屏幕分辨率，完美的动效设计，给用户带来极高的用户体验。 ",-1),g={key:1,class:"introduce"},k=l("div",{class:"title"},"后 端",-1),L=l("div",{class:"content"}," 后端开发即“服务器端”开发，主要涉及软件系统“后端”的东西。比如，用于托管网站和 App 数据的服务器、放置在后端服务器与浏览器及 App 之间的中间件，它们都属于后端。简单地说，那些你在屏幕上看不到但又被用来为前端提供支持的东西就是后端。 ",-1),p={key:2,class:"introduce"},b=l("div",{class:"title"},"Android",-1),y=l("div",{class:"content"}," android开发是指android平台上应用的制作，Android早期由“Android之父”之称的Andy Rubin创办，Google于2005年并购了成立仅22个月的高科技企业Android，展开了短信、手机检索、定位等业务，基于Linux的通用平台进入了开发。 ",-1),A={key:3,class:"introduce"},E=l("div",{class:"title"},"Python",-1),S=l("div",{class:"content"}," Python崇尚优美、清晰、简单，是一个优秀并广泛使用的语言。 Python可以应用于众多领域，如：数据分析、组件集成、网络服务、图像处理、数值计算和科学计算等众多领域。目前业内几乎所有大中型互联网企业都在使用Python，如：Youtube、Dropbox、BT、Quora（中国知乎）、豆瓣、知乎、Google、Yahoo!、Facebook、NASA、百度、腾讯、汽车之家、美团等。 ",-1),x=l("div",{class:"wait"},"再看看",-1);u.render=function(e,s,t,i,n,u){const I=o("router-link");return c(),a("div",m,[l("div",v,[l("div",{onClick:s[1]||(s[1]=s=>e.handleItemClick(2,s)),class:"item iconfont left"},"  "),l("div",{onClick:s[2]||(s[2]=s=>e.handleItemClick(1,s)),class:"item iconfont middle"},"  "),l("div",{onClick:s[3]||(s[3]=s=>e.handleItemClick(3,s)),class:"item iconfont right"},"  "),l("div",{onClick:s[4]||(s[4]=s=>e.handleItemClick(4,s)),class:"item iconfont behind"},"  ")]),1===e.currentCourse?(c(),a("div",h,[f,C])):r("",!0),2===e.currentCourse?(c(),a("div",g,[k,L])):r("",!0),3===e.currentCourse?(c(),a("div",p,[b,y])):r("",!0),4===e.currentCourse?(c(),a("div",A,[E,S])):r("",!0),l("div",{onClick:s[5]||(s[5]=(...s)=>e.handleConfirm&&e.handleConfirm(...s)),class:"confirm"},"选好了"),l(I,{to:"home"},{default:d((()=>[x])),_:1})])};export default u;
