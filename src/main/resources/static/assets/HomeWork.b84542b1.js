import{w as e,F as a,m as l,x as s,B as i,b as o,a as t,C as n,D as r,u,v as d,G as c,c as v}from"./index.529d0d2c.js";import{d as f,j as m,e as p,h as k,r as g,y,K as w,w as I,n as h,g as x,k as _,F as b,q as C,s as S,E as N,x as F,p as O,i as P,at as z,z as L,au as T,av as q}from"./vendor.695cdb15.js";import{a as j,_ as V}from"./Fatal.e8846fc8.js";var B=f({expose:[],props:{type:{type:[String,String,String,String],required:!1}},setup:e=>(a,l)=>(m(),p("span",{class:["tag",e.type||"normal"]},[k(a.$slots,"default",{},void 0,!0)],2))});B.__scopeId="data-v-1cdfb8c9";const W=F();O("data-v-07f642a9");const D={class:"detail"},E={class:"title"},G={class:"light small-font"},K=x("p",{class:"task-name"},"作业文件",-1),M={class:"flex ac list"},$=x("div",{class:"el-icon-download"},null,-1),A={class:"task-name"},H=S(" 提交记录 "),J={key:1},Q=x("span",{style:{"font-size":"14px"}},"无记录",-1),R={class:"flex ac list"},U={class:"file flex ac"},X={class:"file-item"},Y={class:"file-desc"},Z={class:"progress-wrap"},ee=S("新提交");P();var ae=f({expose:[],props:{userId:{type:String,required:!0},courseId:{type:Number,required:!0},info:{type:Object,required:!1}},setup(u){const d=u,c=g(null),v=g(a.Normal),f=async()=>{if(F.value=[],v.value=a.Pending,!d.info)return;const l=await(s=d.userId,i=d.info.id,e.get("/work/queryOneWork",{params:{userId:s,taskId:i}}));var s,i;if(l.error_code!==t.Success)return c.value=null,void(v.value=a.Fail);v.value=a.Success,c.value=l.data},k=l=>{l&&c.value&&z.confirm("删除不可逆，确认删除吗?",{cancelButtonText:"取消",confirmButtonText:"确定",async callback(s){if("confirm"===s){v.value=a.Pending;(await((a,l)=>e.post("/work/deleteWork",{userId:a,id:l}))(d.userId,l)).error_code===t.Success?(c.value=null,f(),v.value=a.Success):v.value=a.Fail}}})},F=g([]),O=l((e=>{if(-1!==F.value.findIndex((a=>a.file.name===e.name&&a.file.size===e.size)))return[!1,"不能重复文件"];const a=e.size<15728640;return a&&F.value.push({file:e,progress:0}),[a,"作业不超过15M"]})),P=g(!1),T=async()=>{if(F.value.length)if(d.info){P.value=!0;try{let i=null;if(!c.value&&(i=await(a=d.info.id,l=d.courseId,s=d.userId,e.post("/work/addWork",{taskId:a,courseId:l,userId:s})),i.error_code!==t.Success))return;const o=F.value.map((e=>r(i&&i.data.toString()||c.value.id,e.file,(a=>{e.progress=a.loaded/a.total})).then((e=>{if(e.error_code!==t.Success)throw e;return e}))));await Promise.all(o),await f()}finally{P.value=!1}var a,l,s}else L({message:"亲太心急了，等几秒后点击"});else L({message:"亲太心急了，等几秒后点击"})};return y((()=>{f()})),(e,l)=>{var r,g;const y=w("loading");return I((m(),p("div",D,[x("h4",E,_(u.info&&u.info.taskName||""),1),u.info?(m(),p(b,{key:0},[x("div",G,[x("div",null,_(h(s)(u.info.addTime))+"发布",1),x("div",null,"截至时间"+_(u.info.closeTime),1)]),u.info.taskFileVOList.length?(m(),p(b,{key:0},[K,x("ul",M,[(m(!0),p(b,null,C(u.info&&u.info.taskFileVOList||[],((e,a)=>(m(),p("li",{class:"p file",key:a},[x("a",{class:"file-item",target:"_blank",href:e.filePath,download:e.fileName},[S(_(e.fileName)+" ",1),$],8,["href","download"])])))),128))])],64)):N("",!0)],64)):N("",!0),x("p",A,[H,c.value?(m(),p("i",{key:0,class:"del-btn el-icon-close",onClick:l[1]||(l[1]=e=>{var a;return k((null==(a=c.value)?void 0:a.id)||null)})})):N("",!0)]),c.value?N("",!0):(m(),p("div",J,[Q])),x("ul",R,[(m(!0),p(b,null,C((null==(r=c.value)?void 0:r.workFileVOList)||[],(e=>(m(),p("li",U,[x("div",X,[x("a",{href:e.filePath,download:e.fileName,target:"_blank"},_(e.fileName),9,["href","download"])]),x("i",{class:"del-btn el-icon-close",onClick:a=>(async e=>{e&&z.confirm("删除不可逆，确认删除吗?",{cancelButtonText:"取消",confirmButtonText:"确定",async callback(a){if("confirm"===a&&(await n(e.toString(),d.userId)).error_code===t.Success){const a=c.value.workFileVOList.findIndex((a=>a.id===e));c.value.workFileVOList.splice(a,1),f()}}})})(null==e?void 0:e.id)},null,8,["onClick"])])))),256))]),x("div",null,[x("div",{ref:O,class:"drop-file p"},"选择文件",512),x("ul",Y,[(m(!0),p(b,null,C(F.value,((e,a)=>(m(),p("li",null,[S(_(e.file.name)+" "+_(h(i)(e.file.size))+" ",1),x("i",{class:"el-icon-close",onClick:e=>{return l=a,void F.value.splice(l,1);var l}},null,8,["onClick"]),x("div",Z,[x("div",{class:"progress-bar",style:{width:100*e.progress+"%"}},null,4)])])))),256))]),x(h(o),{type:"broke",onClick:T,disabled:!!(null==(g=u.info)?void 0:g.isClosed)||P.value,loading:P.value},{default:W((()=>[ee])),_:1},8,["disabled","loading"])])],512)),[[y,!u.info||!u.courseId||!u.userId||v.value===h(a).Pending]])}}});ae.__scopeId="data-v-07f642a9";const le=F();O("data-v-1e2171b0");const se={class:"view-main"},ie={class:"wrap"},oe={class:"info flex jc ac"},te={key:0,class:"list shade"},ne={class:"task-name"},re=S("可延迟提交"),ue={class:"time",style:{"margin-left":"5px"}},de={class:"collapse"},ce=x("p",null,"作业详情",-1),ve={class:"flex ac homework-list"},fe=x("div",{class:"el-icon-download"},null,-1),me=x("br",null,null,-1),pe=S("提交");P();var ke=f({expose:[],setup(l){const i=u(),n=g([]);y((()=>{n.value=i.state.user.allCourses.map((e=>({k:e.courseName,v:e.courseId})))}));const r=g(-1),f=g(""),k=g([]),w=g(0),I=g(a.Normal),F=async()=>{if(r.value<0)return;I.value=a.Pending;const l=await(s=r.value,e.get("/task/queryTasks",{params:{courseId:s}}));var s;l.error_code===t.Success?(k.value=l.data.taskPOList,w.value=l.data.total,I.value=a.Success):I.value=a.Fail};y((()=>{F()}));const O=(e,a)=>{r.value=e,f.value=a},P=g(null),z=g();return(e,l)=>(m(),p("div",se,[x("div",ie,[x("div",oe,[x(d,{class:"logo",courseName:f.value},null,8,["courseName"])]),x(h(c),{class:"shade",items:n.value,onChange:O},null,8,["items"]),k.value.length?(m(),p("div",te,[x(h(T),{accordion:""},{default:le((()=>[(m(!0),p(b,null,C(k.value,(e=>(m(),p(h(q),{key:e.id,class:["item",{done:e.isClosed}]},{title:le((()=>[x("div",ne,_(e.taskName),1),x("div",null,[x(B,{style:{"font-size":"12px","margin-right":"5px"},type:e.isClosed?"error":"normal"},{default:le((()=>[S(_(e.isClosed?"已结束":"进行中"),1)])),_:2},1032,["type"]),e.commitLate?(m(),p(B,{key:0,style:{"font-size":"12px"},type:"success"},{default:le((()=>[re])),_:1})):N("",!0)]),x("div",ue,[x("div",null,_(h(s)(e.addTime)),1)])])),default:le((()=>[x("div",de,[ce,x("ul",ve,[(m(!0),p(b,null,C(e.taskFileVOList,((e,a)=>(m(),p("li",{class:"p file",key:a},[x("a",{href:e.filePath,download:e.fileName,target:"_blank"},[S(_(e.fileName)+" ",1),fe],8,["href","download"])])))),128))]),me,x(h(o),{onClick:a=>(e=>{var a;e.isClosed?L({message:"作业提交通道已关闭，下一次加油叭"}):i.state.user.allCourses[0]?(P.value=e,r.value=-1===r.value?i.state.user.allCourses[0].id:r.value,null==(a=z.value)||a.open()):L({message:"请重新登陆"})})(e)},{default:le((()=>[pe])),_:2},1032,["onClick"])])])),_:2},1032,["class"])))),128))])),_:1})])):I.value===h(a).Success?(m(),p(j,{key:1})):I.value===h(a).Fail?(m(),p(V,{key:2})):N("",!0),x(v,{ref:z,width:"80%"},{default:le((()=>[x(ae,{userId:h(i).state.user.userInfo.userId||"",info:P.value||void 0,courseId:r.value},null,8,["userId","info","courseId"])])),_:1},512)])]))}});ke.__scopeId="data-v-1e2171b0";export default ke;