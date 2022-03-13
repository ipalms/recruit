import{d as e,r as a,B as l,e as s,g as u,k as o,n as t,s as d,x as n,p as r,i,j as c,y as v,W as p,aF as f,az as m,F as y,q as b,J as g,U as h,Z as _,a0 as w,w as k,z as x,at as I,L as C,K as V,Q as U}from"./vendor.695cdb15.js";import{af as z,m as F,B,b as N,w as P,ag as S,R as j,c as q,u as M,ah as T,a as G,ai as A}from"./index.529d0d2c.js";const D=n();r("data-v-0b5043f2");const J={class:"big-file-uploader"},K={class:"file-info"},L={class:"flex ac"},Q={class:"progress-info"},R={key:0},W=d(" 上传失败 "),Z=d("重试"),E={key:1},H={class:"info"},O={class:"progress-wrap"},X={class:"info"},Y={class:"progress-wrap"};var $,ee;i(),(ee=$||($={}))[ee.Pause=0]="Pause",ee[ee.Uploading=1]="Uploading",ee[ee.Pending=2]="Pending";var ae=e({expose:[],props:{courseId:{type:[Number,null],required:!1},id:{type:Number,required:!0}},emits:["finish"],setup(e,{emit:n}){const r=e,i=a(2),v=l((()=>{switch(i.value){case 0:return"恢复";case 1:return"暂停";case 2:return"开始上传"}})),p=()=>{2===i.value?(x.value&&x.value(),i.value=1):0===i.value?(i.value=1,w()):(i.value=0,_())},f=3*Math.pow(1024,3);let m=a();const[y,b,g,h,_,w]=z(r.id,r.courseId||void 0,(e=>{n("finish",e)})),k=l((()=>b.value>=1));let x=a();const I=F((e=>(m.value=e,i.value=2,[e.size<f,"文件上传限制为 3GB，你可以: 出资加大服务器存储容量"])),(async e=>{const a=await h(e);x.value=a}));return(e,a)=>{var l,n;return c(),s("div",J,[u("h3",{class:"p",ref:I},"拖动到此或者点击上传文件",512),u("ul",K,[u("li",null,"文件名: "+o(null==(l=t(m))?void 0:l.name),1),u("li",null,"总大小: "+o(t(B)(null==(n=t(m))?void 0:n.size)),1)]),u("div",L,[u(t(N),{onClick:p,disabled:!t(k)},{default:D((()=>[d(o(t(v)),1)])),_:1},8,["disabled"]),u("span",Q,["上传失败"===t(y)?(c(),s("span",R,[W,u(t(N),{onClick:a[1]||(a[1]=()=>{t(x)&&t(x)()}),type:"broke"},{default:D((()=>[Z])),_:1})])):(c(),s("span",E,o(t(y)),1))])]),u("span",H,"准备进度: "+o(Math.floor(100*t(b))+"%"),1),u("div",O,[u("div",{class:"progress-bar",style:{width:(100*t(b)).toFixed(2)+"%"}},null,4)]),u("span",X,"上传进度: "+o(Math.floor(100*t(g))+"%"),1),u("div",Y,[u("div",{class:"progress-bar",style:{width:(100*t(g)).toFixed(2)+"%"}},null,4)])])}}});ae.__scopeId="data-v-0b5043f2";const le=n();r("data-v-706be548");const se={class:"announce"},ue=u("div",{class:"font16"},"公告标题",-1),oe=u("div",{class:"font16"},"公告内容",-1),te=u("div",{class:"font16"},"公告对象",-1),de=d("全部"),ne=d("上传文件（可选）"),re=d("发布"),ie=d("添加附件"),ce=d("删除公告"),ve=u("h5",{class:"lighter"},[u("div",{style:{padding:"20px","background-color":"rgb(255, 228, 228)",color:"red","border-radius":"5px"}},"不要上传大于3G的文件")],-1),pe={key:1,style:{height:"100%"}};i();var fe=e({expose:[],setup(e){const n=M(),r=a(""),i=a(""),z=a(null),F=a(),B=a(null),N=async()=>{const e=await((e,a,l,s,u)=>{const o=new FormData;return o.set("adminId",e),!S(a)&&o.set("courseId",a.toString()),o.set("title",l),o.set("content",s),u&&o.set("file",u),P.post("/announce/addAnnounce",o)})(n.state.user.userInfo.adminId,z.value,r.value,i.value);return e.error_code===G.Success&&(B.value=e.data,W(),!0)},D=async()=>{await N()&&F.value.open()},J=l((()=>[r,i].every((e=>e.value)))),K=async()=>{await N()&&x({message:"发布成功"})},L=()=>{F.value.close(),x({message:"上传文件成功"})},Q=a(1),R=a(),W=async()=>{if(!z.value)return;const e=await A(Q.value,z.value,j);e.error_code===G.Success&&(R.value=e.data)},Z=e=>{Q.value=e};v((()=>{var e;z.value=(null==(e=n.state.user.allCourses[0])?void 0:e.courseId)||null})),v((()=>{W()}));const E=a(0);return(e,a)=>{var l,v;const N=C("el-table-column"),P=C("el-table"),S=V("loading");return c(),s(y,null,[u("div",se,[ue,u(t(p),{placeholder:"公告标题",modelValue:r.value,"onUpdate:modelValue":a[1]||(a[1]=e=>r.value=e)},null,8,["modelValue"]),oe,u(t(p),{placeholder:"公告内容",rows:2,type:"textarea",modelValue:i.value,"onUpdate:modelValue":a[2]||(a[2]=e=>i.value=e)},null,8,["modelValue"]),te,u(t(f),{modelValue:z.value,"onUpdate:modelValue":a[3]||(a[3]=e=>z.value=e)},{default:le((()=>[u(t(m),{label:null},{default:le((()=>[de])),_:1}),(c(!0),s(y,null,b(t(n).state.user.allCourses,(e=>(c(),s(t(m),{label:e.courseId,key:e.courseId},{default:le((()=>[d(o(e.courseName),1)])),_:2},1032,["label"])))),128))])),_:1},8,["modelValue"]),u(t(g)),u(t(h),{onClick:D,type:"primary"},{default:le((()=>[ne])),_:1}),u(t(h),{onClick:K,disabled:!t(J),type:"primary"},{default:le((()=>[re])),_:1},8,["disabled"])]),u(t(g)),u("div",null,[u(t(_),{modelValue:z.value,"onUpdate:modelValue":a[4]||(a[4]=e=>z.value=e),placeholder:"选择方向"},{default:le((()=>[(c(!0),s(y,null,b(t(n).state.user.allCourses,(e=>(c(),s(t(U),{key:e.id,label:e.courseName,value:e.courseId},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),u(P,{style:{width:"100%"},data:null==(l=R.value)?void 0:l.items},{default:le((()=>[u(N,{prop:"title",label:"标题"}),u(N,{prop:"addTime",label:"发布时间"}),u(N,{label:"操作"},{default:le((e=>[u(t(h),{type:"primary",onClick:a=>(async(e,a)=>{B.value=e,E.value=a,F.value.open()})(e.row.id,e.row.courseId)},{default:le((()=>[ie])),_:2},1032,["onClick"]),u(t(h),{type:"primary",onClick:a=>(async e=>{e&&I.confirm("确认删除吗",{cancelButtonText:"取消",confirmButtonText:"确定",callback:async a=>("confirm"===a&&(await T(e)).error_code===G.Success&&(x({message:"删除成功"}),W()),!0)})})(e.row.id)},{default:le((()=>[ce])),_:2},1032,["onClick"])])),_:1})])),_:1},8,["data"]),u(t(w),{onCurrentChange:Z,"hide-on-single-page":"","page-size":t(j),total:null==(v=R.value)?void 0:v.total},null,8,["page-size","total"]),u(q,{ref:F,width:"80%"},{default:le((()=>[B.value?(c(),s(y,{key:0},[ve,u(ae,{onFinish:L,id:B.value,"course-id":z.value},null,8,["id","course-id"])],64)):k((c(),s("div",pe,null,512)),[[S,!0]])])),_:1},512)])],64)}}});fe.__scopeId="data-v-706be548";var me=e({expose:[],setup:e=>(e,a)=>(c(),s("div",null,[u(fe)]))});me.__scopeId="data-v-5139fb7a";export default me;
