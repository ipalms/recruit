import{am as e,az as a,a$ as l,r as t,af as o,g as s,b0 as n,b1 as i,d as r,b2 as c,b3 as d,b4 as m,b5 as u,b6 as f,aH as p,b7 as h,R as g,S as w,b8 as y,t as b,b9 as k,ba as C,aM as v,bb as F,aB as P,o as x,c as B,a as T,n as _,a5 as E,a6 as N,C as S,k as H}from"./index.77974a83.js";import{_ as L}from"./AdBeacon.c4bfd460.js";import{i as M}from"./index.db8a4765.js";var I=r({components:{ElTable:c,ElTableColumn:d,ElForm:m,ElFormItem:u,ElPagination:f,ElButton:p,AdBeacon:L,ElTag:h},setup(){const r=o(),c=b(),d=t([]),m=t(),u=r.params.id,f=g.get("adminId"),p=t(""),{handleMarkScore:h}=function(){async function t(e,a){return await l(e,a)}return{handleMarkScore:function(l,o){e.prompt("请输入分数","",{confirmButtonText:"确定",cancelButtonText:"取消",inputPlaceholder:"分数为1-10之间的数字",inputPattern:/^(10|[1-9])$/,inputErrorMessage:"分数格式不正确"}).then((async({value:e})=>{const s=Number.parseInt(e),n=await t(l,s);console.log(n),200===n.error_code?(o.score=s,a({type:"success",message:"打分成功！"})):a({type:"error",message:"网络错误！"})}))},handleMarkScoreConfirm:t}}(),{comPersonList:P,handlePaginationChange:x,total:B}=function(){const e=o(),a=t(),l=t(2),i=e.params.id;function r(e){return console.log(e),s((()=>(e.data.items.forEach((e=>{e.userName=e.userVO.userName})),e.data.items))).value}return{comPersonList:r,handlePaginationChange:async function(e){const l=await n(i,e,5);console.log(l),a.value=r(l)},total:l}}();return w((async()=>{const e=await n(u,1,5);B.value=s((()=>e.data.total)).value,m.value=P(e),d.value=c.state.homework.currentFiles,async function(e){const a=await i(e),l=document.getElementById("diagram"),t=M(l),o={title:{text:"提交情况",subtext:"该作业的提交百分比",left:"center"},tooltip:{trigger:"item"},legend:{orient:"vertical",left:"left"},series:[{type:"pie",radius:"50%",data:[{value:a.data.submitPeople,name:"已提交"},{value:a.data.unSubmitPeople,name:"未提交"}],emphasis:{itemStyle:{shadowBlur:10,shadowOffsetX:0,shadowColor:"rgba(0, 0, 0, 0.5)"}}}]};o&&t.setOption(o)}(u);const a=await y(u);p.value=a.data})),{homeworkFileList:d,personList:m,total:B,handlePaginationChange:x,handleMarkScore:h,downloadFileUrl:p,handleCloseHomework:function(){e.confirm("此操作将关闭此作业的提交通道, 是否继续?","",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((async()=>{const e=await k({id:u,adminId:f});console.log(e),200===e.error_code?a({type:"success",message:"关闭成功！"}):a({type:"error",message:"网络错误！"})})).catch((()=>{a({type:"warning",message:"取消关闭"})}))},handleUploadHomeworkFiles:async function(e){const l=await C(u,e.target.files[0]);if(console.log(l),200===l.error_code){const l=e.target.files[0].name;c.commit({type:`${v.uploadFile}`,payload:{fileName:l,addTime:new Date,filePath:""}}),a({type:"success",message:"附件上传成功！"})}else a({type:"error",message:"上传失败！"})},handleDeleteTaskFile:async function(l,t){e.confirm("此操作将删除这个附件, 是否继续?","",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((async()=>{const e=await F(t,f);console.log(e),200==e.error_code?(c.commit({type:`${v.deleteFile}`,payload:l}),a({type:"success",message:"删除成功！"})):a({type:"error",message:"网咯错误！"})})).catch((e=>{a({type:"warning",message:e})}))}}}});const U={class:"homework-detail"},O=T("li",{class:"title"},"作业提交详情",-1),$={class:"table"},j=H("打分"),A=T("li",{id:"diagram"},null,-1),D={class:"upload-homework-file"},z=H("上传附件 "),V={class:"files"},R=T("li",{class:"file-title"},"任务附件 :",-1),X={class:"download-file"},q=T("li",{class:"download-homework"},"作业 :",-1);I.render=function(e,a,l,t,o,s){const n=P("AdBeacon"),i=P("router-link"),r=P("el-form-item"),c=P("el-form"),d=P("el-table-column"),m=P("ElButton"),u=P("el-table"),f=P("el-pagination"),p=P("el-tag");return x(),B("ul",U,[T(i,{to:"/checkTask"},{default:_((()=>[T(n,{title:"作业管理",top:-4.5},null,8,["top"])])),_:1}),O,T("li",$,[T(u,{data:e.personList,style:{width:"100%",margin:"0 auto"}},{default:_((()=>[T(d,{type:"expand"},{default:_((e=>[T(c,{"label-position":"left",inline:"",class:"demo-table-expand"},{default:_((()=>[T(r,{class:"homework-file-warp",label:"附件"},{default:_((()=>[(x(!0),B(E,null,N(e.row.workFileVOList,(e=>(x(),B("span",{class:"homework-file",key:e.filePath},[T("a",{style:{color:"#24acf2"},href:e.filePath},S(e.fileName),9,["href"])])))),128))])),_:2},1024)])),_:2},1024)])),_:1}),T(d,{label:"姓名",prop:"userName"}),T(d,{label:"学号",prop:"userId"}),T(d,{label:"分数",prop:"score"}),T(d,{label:"操作",prop:"desc"},{default:_((a=>[T(m,{size:"mini",round:"",onClick:l=>e.handleMarkScore(a.row.id,a.row)},{default:_((()=>[j])),_:2},1032,["onClick"])])),_:1})])),_:1},8,["data"]),T(f,{onCurrentChange:e.handlePaginationChange,class:"pagination-style",layout:"prev, pager, next",total:e.total},null,8,["onCurrentChange","total"])]),A,T("div",{onClick:a[1]||(a[1]=(...a)=>e.handleCloseHomework&&e.handleCloseHomework(...a)),class:"close-homework"},"关闭"),T("div",D,[T("input",{type:"file",onChange:a[2]||(a[2]=(...a)=>e.handleUploadHomeworkFiles&&e.handleUploadHomeworkFiles(...a))},null,32),z]),T("li",null,[T("ul",V,[R,(x(!0),B(E,null,N(e.homeworkFileList,(a=>(x(),B("li",{class:"file-name",key:a.fileName},[T(p,{onClose:l=>e.handleDeleteTaskFile(a.fileName,a.id),key:a.fileName,closable:""},{default:_((()=>[T("a",{class:"file-a",href:a.filePath},S(a.fileName),9,["href"])])),_:2},1032,["onClose"])])))),128))])]),T("ul",X,[q,T("a",{class:"download-file-start",href:e.downloadFileUrl},"点击下载所有作业",8,["href"])])])};export default I;
