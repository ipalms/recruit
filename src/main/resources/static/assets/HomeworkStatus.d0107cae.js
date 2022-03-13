import{_ as e}from"./virtualList.1bc98cdb.js";import{_ as t}from"./AdBeacon.0a2effe4.js";import{d as a,r as n,o as l,B as i,L as s,j as o,e as r,g as u,l as m,k as d,p as c,i as v,x as h,$ as p,Y as g}from"./vendor.695cdb15.js";import{aa as f}from"./index.529d0d2c.js";import{i as x}from"./index.1c41e40c.js";function y(e){function t(e){return e.length?e.reduce(((e,t)=>e+t))/e.length:0}const a=function e(t){if(t.length<=1)return t;const a=Math.floor(t.length/2),n=t.splice(a,1)[0],l=[],i=[];for(let s=0;s<t.length;s++)t[s]<n?l.push(t[s]):i.push(t[s]);return e(l).concat([n],e(i))}(e),n=(l=a).length%2==0?(l[l.length/2]+l[l.length/2-1])/2:l[Math.floor(l.length/2)];var l;const i=parseFloat(t(a).toFixed(2)),s=function(e){return e[e.length-1]-e[0]}(a),o=function(e){const t=e.length/4,a=e.length-1-e.length/4,n=(e[Math.ceil(t)]-e[Math.floor(t)])*(t-Math.floor(t))+e[Math.floor(t)],l=(e[Math.ceil(a)]-e[Math.floor(a)])*(a-Math.floor(a))+e[Math.floor(a)];return e.length>=2?l-n:0}(a),r=function(e){const a=t(e);let n=0;return e.forEach((e=>{n+=Math.pow(e-a,2)})),parseFloat((n/e.length).toFixed(2))}(a),u=parseFloat(Math.pow(r,.5).toFixed(2));return e.length?[n,i,s,o,r,u]:[0,0,0,0,0,0]}function b(e){const t=[];return e.forEach((e=>{t.push(e.avgScore)})),t}var L=a({components:{VirtualList:e},setup(){const e=n([]);return l((async()=>{const t=await f(1);console.log(t),e.value=i((()=>t.data.users)).value;const a=b(e.value),[n,l,s,o,r,u]=y(a),m=document.getElementById("front"),d=x(m),c={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,s,o,r,u],name:"实际值"}]}]};c&&d.setOption(c)})),{frontPersonList:e}}});const A={class:"front-homework-status"},_={class:"item"},B=u("div",{id:"front"},null,-1);L.render=function(e,t,a,n,l,i){const c=s("virtual-list");return o(),r("div",A,[u(c,{containerWidth:120,size:10,list:e.frontPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:e})=>[u("ul",_,[u("li",null,d(e.userName),1),u("li",null,d(e.userId),1),u("li",null,d(e.major),1),u("li",null,d(e.submitCount),1),u("li",null,d(e.avgScore),1)])])),_:1},8,["list"]),B])};var C=a({components:{VirtualList:e},setup(){const e=n([]);return l((async()=>{const t=await f(2);e.value=i((()=>t.data.users)).value;const a=b(e.value),[n,l,s,o,r,u]=y(a),m=document.getElementById("end"),d=x(m),c={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,s,o,r,u],name:"实际值"}]}]};c&&d.setOption(c)})),{endPersonList:e}}});const P={class:"end-homework-status"},w={class:"item"},E=u("div",{id:"end"},null,-1);C.render=function(e,t,a,n,l,i){const c=s("virtual-list");return o(),r("div",P,[u(c,{containerWidth:120,size:10,list:e.endPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:e})=>[u("ul",w,[u("li",null,d(e.userName),1),u("li",null,d(e.userId),1),u("li",null,d(e.major),1),u("li",null,d(e.submitCount),1),u("li",null,d(e.avgScore),1)])])),_:1},8,["list"]),E])};var S=a({components:{VirtualList:e},setup(){const e=n([]);return l((async()=>{const t=await f(3);e.value=i((()=>t.data.users)).value;const a=b(e.value),[n,l,s,o,r,u]=y(a),m=document.getElementById("python"),d=x(m),c={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,s,o,r,u],name:"实际值"}]}]};c&&d.setOption(c)})),{pythonPersonList:e}}});const I=h();c("data-v-7e672c92");const j={class:"python-homework-status"},H={class:"item"},M=u("div",{id:"python"},null,-1);v();const k=I(((e,t,a,n,l,i)=>{const m=s("virtual-list");return o(),r("div",j,[u(m,{containerWidth:120,size:10,list:e.pythonPersonList,itemHeight:45,title:"",containerHeight:470},{default:I((({item:e})=>[u("ul",H,[u("li",null,d(e.userName),1),u("li",null,d(e.userId),1),u("li",null,d(e.major),1),u("li",null,d(e.submitCount),1),u("li",null,d(e.avgScore),1)])])),_:1},8,["list"]),M])}));S.render=k,S.__scopeId="data-v-7e672c92";var V=a({components:{VirtualList:e},setup(){const e=n([]);return l((async()=>{const t=await f(4);e.value=i((()=>t.data.users)).value;const a=b(e.value),[n,l,s,o,r,u]=y(a),m=document.getElementById("android"),d=x(m),c={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,s,o,r,u],name:"实际值"}]}]};c&&d.setOption(c)})),{androidPersonList:e}}});const N={class:"android-homework-status"},F={class:"item"},O=u("div",{id:"android"},null,-1);V.render=function(e,t,a,n,l,i){const c=s("virtual-list");return o(),r("div",N,[u(c,{containerWidth:120,size:10,list:e.androidPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:e})=>[u("ul",F,[u("li",null,d(e.userName),1),u("li",null,d(e.userId),1),u("li",null,d(e.major),1),u("li",null,d(e.submitCount),1),u("li",null,d(e.avgScore),1)])])),_:1},8,["list"]),O])};var z=a({components:{VirtualList:e},setup(){const e=n([]);return l((async()=>{const t=await f(5);console.log(t),e.value=i((()=>t.data.users)).value;const a=b(e.value),[n,l,s,o,r,u]=y(a),m=document.getElementById("design"),d=x(m),c={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,s,o,r,u],name:"实际值"}]}]};c&&d.setOption(c)})),{designPersonList:e}}});const W={class:"design-homework-status"},T={class:"item"},D=u("div",{id:"design"},null,-1);z.render=function(e,t,a,n,l,i){const c=s("virtual-list");return o(),r("div",W,[u(c,{containerWidth:120,size:10,list:e.designPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:e})=>[u("ul",T,[u("li",null,d(e.userName),1),u("li",null,d(e.userId),1),u("li",null,d(e.major),1),u("li",null,d(e.submitCount),1),u("li",null,d(e.avgScore),1)])])),_:1},8,["list"]),D])};var U=a({components:{VirtualList:e},setup(){const e=n([]);return l((async()=>{const t=await f(6);e.value=i((()=>t.data.users)).value;const a=b(e.value),[n,l,s,o,r,u]=y(a),m=document.getElementById("operation"),d=x(m),c={tooltip:{},legend:{data:["预算分配（Allocated Budget）","实际开销（Actual Spending）"]},radar:{indicator:[{name:"中位数",max:10},{name:"平均分",max:10},{name:"极差",max:10},{name:"四分位差",max:10},{name:"方差",max:3},{name:"标准差",max:3}]},series:[{name:"预期 vs 实际",type:"radar",data:[{value:[10,10,10,10,3,3],name:"峰值"},{value:[n,l,s,o,r,u],name:"实际值"}]}]};c&&d.setOption(c)})),{operationPersonList:e}}});const Y={class:"operation-homework-status"},$={class:"item"},q=u("div",{id:"operation"},null,-1);U.render=function(e,t,a,n,l,i){const c=s("virtual-list");return o(),r("div",Y,[u(c,{containerWidth:120,size:10,list:e.operationPersonList,itemHeight:45,title:"",containerHeight:470},{default:m((({item:e})=>[u("ul",$,[u("li",null,d(e.userName),1),u("li",null,d(e.userId),1),u("li",null,d(e.major),1),u("li",null,d(e.submitCount),1),u("li",null,d(e.avgScore),1)])])),_:1},8,["list"]),q])};var G=a({components:{VirtualList:e,AdBeacon:t,ElTabPane:p,ElTabs:g,FrontEndCom:L,AndroidCom:V,DesignCom:z,OperationCom:U,EndCom:C,PythonCom:S},setup:()=>({tableActiveeName:n("first")})});const J={class:"homework-status"},K=u("h1",{class:"title"},"完成状况",-1);G.render=function(e,t,a,n,l,i){const d=s("AdBeacon"),c=s("router-link"),v=s("FrontEndCom"),h=s("el-tab-pane"),p=s("EndCom"),g=s("PythonCom"),f=s("AndroidCom"),x=s("design-com"),y=s("operation-com"),b=s("el-tabs");return o(),r("div",J,[K,u(c,{to:"/checkTask"},{default:m((()=>[u(d,{boxStyle:"left",title:"作业管理"})])),_:1}),u(b,{stretch:!0,"tab-position":"left",modelValue:e.tableActiveeName,"onUpdate:modelValue":t[1]||(t[1]=t=>e.tableActiveeName=t),class:"table"},{default:m((()=>[u(h,{class:"item",label:"前端",name:"first"},{default:m((()=>[u(v)])),_:1}),u(h,{class:"item",label:"后端",name:"second"},{default:m((()=>[u(p)])),_:1}),u(h,{class:"item",label:"Python",name:"third"},{default:m((()=>[u(g)])),_:1}),u(h,{class:"item",label:"移动",name:"fourth"},{default:m((()=>[u(f)])),_:1}),u(h,{class:"item",label:"设计",name:"fiveth"},{default:m((()=>[u(x)])),_:1}),u(h,{class:"item",label:"产品",name:"sixth"},{default:m((()=>[u(y)])),_:1})])),_:1},8,["modelValue"])])};export default G;
