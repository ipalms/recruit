import{ak as e,ax as a,aZ as l,r as t,ae as o,g as s,a_ as n,a$ as i,d as r,b0 as c,b1 as d,b2 as m,b3 as u,b4 as f,aF as p,b5 as h,R as g,S as w,b6 as y,t as k,b7 as b,b8 as C,aK as F,b9 as v,az as x,o as P,c as _,a as B,n as T,a4 as E,a5 as N,C as S,k as L}from"./index.ea109c08.js";import{_ as H}from"./AdBeacon.c998244f.js";import{i as I}from"./index.db8a4765.js";var M=r({components:{ElTable:c,ElTableColumn:d,ElForm:m,ElFormItem:u,ElPagination:f,ElButton:p,AdBeacon:H,ElTag:h},setup(){const r=o(),c=k(),d=t([]),m=t(),u=r.params.id,f=g.get("adminId"),p=t(""),{handleMarkScore:h}=function(){async function t(e,a){return await l(e,a)}return{handleMarkScore:function(l,o){e.prompt("请输入分数","",{confirmButtonText:"确定",cancelButtonText:"取消",inputPlaceholder:"分数为1-10之间的数字",inputPattern:/^(10|[1-9])$/,inputErrorMessage:"分数格式不正确"}).then((async({value:e})=>{const s=Number.parseInt(e),n=await t(l,s);console.log(n),200===n.error_code?(o.score=s,a({type:"success",message:"打分成功！"})):a({type:"error",message:"网络错误！"})}))},handleMarkScoreConfirm:t}}(),{comPersonList:x,handlePaginationChange:P,total:_}=function(){const e=o(),a=t(),l=t(2),i=e.params.id;function r(e){return console.log(e),s((()=>(e.data.items.forEach((e=>{e.userName=e.userVO.userName})),e.data.items))).value}return{comPersonList:r,handlePaginationChange:async function(e){const l=await n(i,e,5);console.log(l),a.value=r(l)},total:l}}();return w((async()=>{const e=await n(u,1,5);_.value=s((()=>e.data.total)).value,m.value=x(e),d.value=c.state.homework.currentFiles,async function(e){const a=await i(e),l=document.getElementById("diagram"),t=I(l),o={title:{text:"提交情况",subtext:"该作业的提交百分比",left:"center"},tooltip:{trigger:"item"},legend:{orient:"vertical",left:"left"},series:[{type:"pie",radius:"50%",data:[{value:a.data.submitPeople,name:"已提交"},{value:a.data.unSubmitPeople,name:"未提交"}],emphasis:{itemStyle:{shadowBlur:10,shadowOffsetX:0,shadowColor:"rgba(0, 0, 0, 0.5)"}}}]};o&&t.setOption(o)}(u);const a=await y(u);p.value=a.data})),{homeworkFileList:d,personList:m,total:_,handlePaginationChange:P,handleMarkScore:h,downloadFileUrl:p,handleCloseHomework:function(){e.confirm("此操作将关闭此作业的提交通道, 是否继续?","",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((async()=>{const e=await b({id:u,adminId:f});console.log(e),200===e.error_code?a({type:"success",message:"关闭成功！"}):a({type:"error",message:"网络错误！"})})).catch((()=>{a({type:"warning",message:"取消关闭"})}))},handleUploadHomeworkFiles:async function(e){const l=await C(u,e.target.files[0]);if(console.log(l),200===l.error_code){const l=e.target.files[0].name;c.commit({type:`${F.uploadFile}`,payload:{fileName:l,addTime:new Date,filePath:""}}),a({type:"success",message:"附件上传成功！"})}else a({type:"error",message:"上传失败！"})},handleDeleteTaskFile:async function(l,t){e.confirm("此操作将删除这个附件, 是否继续?","",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((async()=>{const e=await v(t,f);console.log(e),200==e.error_code?(c.commit({type:`${F.deleteFile}`,payload:l}),a({type:"success",message:"删除成功！"})):a({type:"error",message:"网咯错误！"})})).catch((e=>{a({type:"warning",message:e})}))}}}});const U={class:"homework-detail"},O=B("li",{class:"title"},"作业提交详情",-1),$={class:"table"},j=L("打分"),A=B("li",{id:"diagram"},null,-1),D={class:"upload-homework-file"},z=L("上传附件 "),V={class:"files"},K=B("li",{class:"file-title"},"任务附件 :",-1),R={class:"download-file"},X=B("li",{class:"download-homework"},"作业 :",-1);M.render=function(e,a,l,t,o,s){const n=x("AdBeacon"),i=x("router-link"),r=x("el-form-item"),c=x("el-form"),d=x("el-table-column"),m=x("ElButton"),u=x("el-table"),f=x("el-pagination"),p=x("el-tag");return P(),_("ul",U,[B(i,{to:"/checkTask"},{default:T((()=>[B(n,{title:"作业管理",top:0})])),_:1}),O,B("li",$,[B(u,{data:e.personList,style:{width:"100%",margin:"0 auto"}},{default:T((()=>[B(d,{type:"expand"},{default:T((e=>[B(c,{"label-position":"left",inline:"",class:"demo-table-expand"},{default:T((()=>[B(r,{class:"homework-file-warp",label:"附件"},{default:T((()=>[(P(!0),_(E,null,N(e.row.workFileVOList,(e=>(P(),_("span",{class:"homework-file",key:e.filePath},[B("a",{style:{color:"#24acf2"},href:e.filePath},S(e.fileName),9,["href"])])))),128))])),_:2},1024)])),_:2},1024)])),_:1}),B(d,{label:"姓名",prop:"userName"}),B(d,{label:"学号",prop:"userId"}),B(d,{label:"分数",prop:"score"}),B(d,{label:"操作",prop:"desc"},{default:T((a=>[B(m,{size:"mini",round:"",onClick:l=>e.handleMarkScore(a.row.id,a.row)},{default:T((()=>[j])),_:2},1032,["onClick"])])),_:1})])),_:1},8,["data"]),B(f,{onCurrentChange:e.handlePaginationChange,class:"pagination-style",layout:"prev, pager, next",total:e.total},null,8,["onCurrentChange","total"])]),A,B("div",{onClick:a[1]||(a[1]=(...a)=>e.handleCloseHomework&&e.handleCloseHomework(...a)),class:"close-homework"},"关闭"),B("div",D,[B("input",{type:"file",onChange:a[2]||(a[2]=(...a)=>e.handleUploadHomeworkFiles&&e.handleUploadHomeworkFiles(...a))},null,32),z]),B("li",null,[B("ul",V,[K,(P(!0),_(E,null,N(e.homeworkFileList,(a=>(P(),_("li",{class:"file-name",key:a.fileName},[B(p,{onClose:l=>e.handleDeleteTaskFile(a.fileName,a.id),key:a.fileName,closable:""},{default:T((()=>[B("a",{class:"file-a",href:a.filePath},S(a.fileName),9,["href"])])),_:2},1032,["onClose"])])))),128))])]),B("ul",R,[X,B("a",{class:"download-file-start",href:e.downloadFileUrl},"点击下载所有作业",8,["href"])])])};export default M;