import{_ as t}from"./virtualList.55416cd7.js";import{_ as e}from"./AdBeacon.79d6660b.js";import{d as a,r as n,S as l,bc as o,g as s,aB as i,o as r,c as u,a as d,n as m,C as c,p as h,e as v,w as f,bd as p,be as g}from"./index.743625ef.js";import{i as x}from"./index.1d4dfd9b.js";function b(t){function e(t){return t.length?t.reduce(((t,e)=>t+e))/t.length:0}const a=function t(e){if(e.length<=1)return e;const a=Math.floor(e.length/2),n=e.splice(a,1)[0],l=[],o=[];for(let s=0;s<e.length;s++)e[s]<n?l.push(e[s]):o.push(e[s]);return t(l).concat([n],t(o))}(t),n=(l=a).length%2==0?(l[l.length/2]+l[l.length/2-1])/2:l[Math.floor(l.length/2)];var l;const o=parseFloat(e(a).toFixed(2)),s=function(t){return t[t.length-1]-t[0]}(a),i=function(t){const e=t.length/4,a=t.length-1-t.length/4,n=(t[Math.ceil(e)]-t[Math.floor(e)])*(e-Math.floor(e))+t[Math.floor(e)],l=(t[Math.ceil(a)]-t[Math.floor(a)])*(a-Math.floor(a))+t[Math.floor(a)];return t.length>=2?l-n:0}(a),r=function(t){const a=e(t);let n=0;return t.forEach((t=>{n+=Math.pow(t-a,2)})),parseFloat((n/t.length).toFixed(2))}(a),u=parseFloat(Math.pow(r,.5).toFixed(2));return t.length?[n,o,s,i,r,u]:[0,0,0,0,0,0]}function y(t){const e=[];return t.forEach((t=>{e.push(t.avgScore)})),e}var A=a({components:{VirtualList:t},setup(){const t=n([]);return l((async()=>{const e=await o(1);console.log(e),t.value=s((()=>e.data.users)).value;const a=y(t.value),[n,l,i,r,u,d]=b(a),m=document.getElementById("front"),c=x(m),h={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,i,r,u,d],name:"实际值"}]}]};h&&c.setOption(h)})),{frontPersonList:t}}});const L={class:"front-homework-status"},_={class:"item"},C=d("div",{id:"front"},null,-1);A.render=function(t,e,a,n,l,o){const s=i("virtual-list");return r(),u("div",L,[d(s,{containerWidth:120,size:10,list:t.frontPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:t})=>[d("ul",_,[d("li",null,c(t.userName),1),d("li",null,c(t.userId),1),d("li",null,c(t.major),1),d("li",null,c(t.submitCount),1),d("li",null,c(t.avgScore),1)])])),_:1},8,["list"]),C])};var w=a({components:{VirtualList:t},setup(){const t=n([]);return l((async()=>{const e=await o(2);t.value=s((()=>e.data.users)).value;const a=y(t.value),[n,l,i,r,u,d]=b(a),m=document.getElementById("end"),c=x(m),h={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,i,r,u,d],name:"实际值"}]}]};h&&c.setOption(h)})),{endPersonList:t}}});const B={class:"end-homework-status"},E={class:"item"},M=d("div",{id:"end"},null,-1);w.render=function(t,e,a,n,l,o){const s=i("virtual-list");return r(),u("div",B,[d(s,{containerWidth:120,size:10,list:t.endPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:t})=>[d("ul",E,[d("li",null,c(t.userName),1),d("li",null,c(t.userId),1),d("li",null,c(t.major),1),d("li",null,c(t.submitCount),1),d("li",null,c(t.avgScore),1)])])),_:1},8,["list"]),M])};var P=a({components:{VirtualList:t},setup(){const t=n([]);return l((async()=>{const e=await o(3);t.value=s((()=>e.data.users)).value;const a=y(t.value),[n,l,i,r,u,d]=b(a),m=document.getElementById("python"),c=x(m),h={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,i,r,u,d],name:"实际值"}]}]};h&&c.setOption(h)})),{pythonPersonList:t}}});const S=f();h("data-v-7852b2cf");const I={class:"python-homework-status"},j={class:"item"},k=d("div",{id:"python"},null,-1);v();const F=S(((t,e,a,n,l,o)=>{const s=i("virtual-list");return r(),u("div",I,[d(s,{containerWidth:120,size:10,list:t.pythonPersonList,itemHeight:45,title:"",containerHeight:470},{default:S((({item:t})=>[d("ul",j,[d("li",null,c(t.userName),1),d("li",null,c(t.userId),1),d("li",null,c(t.major),1),d("li",null,c(t.submitCount),1),d("li",null,c(t.avgScore),1)])])),_:1},8,["list"]),k])}));P.render=F,P.__scopeId="data-v-7852b2cf";var H=a({components:{VirtualList:t},setup(){const t=n([]);return l((async()=>{const e=await o(4);t.value=s((()=>e.data.users)).value;const a=y(t.value),[n,l,i,r,u,d]=b(a),m=document.getElementById("android"),c=x(m),h={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,i,r,u,d],name:"实际值"}]}]};h&&c.setOption(h)})),{androidPersonList:t}}});const V={class:"android-homework-status"},N={class:"item"},z=d("div",{id:"android"},null,-1);H.render=function(t,e,a,n,l,o){const s=i("virtual-list");return r(),u("div",V,[d(s,{containerWidth:120,size:10,list:t.androidPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:t})=>[d("ul",N,[d("li",null,c(t.userName),1),d("li",null,c(t.userId),1),d("li",null,c(t.major),1),d("li",null,c(t.submitCount),1),d("li",null,c(t.avgScore),1)])])),_:1},8,["list"]),z])};var O=a({components:{VirtualList:t,AdBeacon:e,ElTabPane:p,ElTabs:g,FrontEndCom:A,AndroidCom:H,EndCom:w,PythonCom:P},setup:()=>({tableActiveeName:n("first")})});const W={class:"homework-status"},T=d("h1",{class:"title"},"完成状况",-1);O.render=function(t,e,a,n,l,o){const s=i("AdBeacon"),c=i("router-link"),h=i("FrontEndCom"),v=i("el-tab-pane"),f=i("EndCom"),p=i("PythonCom"),g=i("AndroidCom"),x=i("el-tabs");return r(),u("div",W,[T,d(c,{to:"/checkTask"},{default:m((()=>[d(s,{boxStyle:"left",title:"作业管理"})])),_:1}),d(x,{stretch:!0,"tab-position":"left",modelValue:t.tableActiveeName,"onUpdate:modelValue":e[1]||(e[1]=e=>t.tableActiveeName=e),class:"table"},{default:m((()=>[d(v,{class:"item",label:"前端",name:"first"},{default:m((()=>[d(h)])),_:1}),d(v,{class:"item",label:"后端",name:"second"},{default:m((()=>[d(f)])),_:1}),d(v,{class:"item",label:"Python",name:"third"},{default:m((()=>[d(p)])),_:1}),d(v,{class:"item",label:"移动",name:"fourth"},{default:m((()=>[d(g)])),_:1})])),_:1},8,["modelValue"])])};export default O;
