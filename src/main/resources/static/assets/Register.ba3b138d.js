import{p as a,i as e,d as s,a5 as l,j as d,e as o,g as r,n as u,x as m,s as n,r as t,A as i,ak as c,al as p,B as v,a7 as f,z as g}from"./vendor.695cdb15.js";import{a as w,s as V,F as h,r as _,l as I,_ as j,b,c as x}from"./index.062403ca.js";import{d as C,e as U,b as N,c as k,_ as y}from"./hooks.43ea23dc.js";import{_ as S,a as A,b as q}from"./SendAuthCode.9df7fe1f.js";const z=m();a("data-v-9d5a5ad8");const B={class:"wrap"},F=n("我们需要验证你的邮箱");e();var P=s({expose:[],setup(a){const e=l("userId"),s=l("email"),m=V(1),n=()=>{m((null==e?void 0:e.value)||"",(null==s?void 0:s.value)||"")},t=l("push"),i=l("register"),c=async a=>{const e=await i(a);e&&e.error_code===w.Success&&t()};return(a,e)=>(d(),o("div",B,[r(S,{address:u(s),request:n,confirm:c},{header:z((()=>[F])),_:1},8,["address"])]))}});P.__scopeId="data-v-9d5a5ad8";var D=s({expose:[],setup(a){const e=[P,q],s=t(null),{expose:l}=i();return l({reset:()=>{s.value.reset()}}),(a,l)=>(d(),o(A,{ref:s,stack:e},null,512))}});D.__scopeId="data-v-56f0e2cd";const E=m();a("data-v-0806278c");const G={class:"register"},H={class:"row"},J={class:"input"},K={class:"row"},L={class:"input"},M={class:"row"},O={class:"input"},Q={class:"row"},R={class:"input"},T={class:"row"},W={class:"input"},X={class:"row"},Y={class:"input"},Z=n("注册");e();var $=s({expose:[],setup(a){const e=c({userId:"",userName:"",password:"",passwordConfirm:"",email:"",major:""}),[s,l]=C(p(e,"email")),[m,n]=U(p(e,"userId")),[i,w]=N(p(e,"userName")),[V,S]=N(p(e,"major")),[A,q]=N(p(e,"password")),[z,B]=k(p(e,"password"))(p(e,"passwordConfirm")),F=t([s,m,i,V,A,z]),P=v((()=>{let a=0;for(;a<F.value.length;){if(F.value[a].value!==h.Success)return!1;a++}return!0})),$=t(null),aa=()=>{$.value.open()},ea=t(null),sa=()=>{ea.value&&ea.value.reset()};return f("close",(()=>{$.value.close()})),f("register",(a=>P.value?_(e.userId,e.userName,e.password,e.email,e.major,a):(g({message:"请检查信息是否全部无误填写完毕"}),Promise.resolve(null)))),f("userId",p(e,"userId")),f("email",p(e,"email")),I(),(a,t)=>(d(),o("div",G,[r("div",H,[r("div",J,[r(u(j),{modelValue:u(e).userId,"onUpdate:modelValue":t[1]||(t[1]=a=>u(e).userId=a),placeholder:"学号"},null,8,["modelValue"])]),r(y,{flag:u(m),msg:u(n)},null,8,["flag","msg"])]),r("div",K,[r("div",L,[r(u(j),{modelValue:u(e).userName,"onUpdate:modelValue":t[2]||(t[2]=a=>u(e).userName=a),placeholder:"姓名"},null,8,["modelValue"])]),r(y,{flag:u(i),msg:u(w)},null,8,["flag","msg"])]),r("div",M,[r("div",O,[r(u(j),{modelValue:u(e).major,"onUpdate:modelValue":t[3]||(t[3]=a=>u(e).major=a),placeholder:"专业"},null,8,["modelValue"])]),r(y,{flag:u(V),msg:u(S)},null,8,["flag","msg"])]),r("div",Q,[r("div",R,[r(u(j),{type:"password",modelValue:u(e).password,"onUpdate:modelValue":t[4]||(t[4]=a=>u(e).password=a),placeholder:"密码"},null,8,["modelValue"])]),r(y,{flag:u(A),msg:u(q)},null,8,["flag","msg"])]),r("div",T,[r("div",W,[r(u(j),{type:"password",modelValue:u(e).passwordConfirm,"onUpdate:modelValue":t[5]||(t[5]=a=>u(e).passwordConfirm=a),placeholder:"确认密码"},null,8,["modelValue"])]),r(y,{flag:u(z),msg:u(B)},null,8,["flag","msg"])]),r("div",X,[r("div",Y,[r(u(j),{modelValue:u(e).email,"onUpdate:modelValue":t[6]||(t[6]=a=>u(e).email=a),placeholder:"邮箱"},null,8,["modelValue"])]),r(y,{msg:u(l),flag:u(s)},null,8,["msg","flag"])]),r(u(b),{class:"btn",onClick:aa,disabled:!u(P)},{default:E((()=>[Z])),_:1},8,["disabled"]),r(x,{ref:$,width:"80%",height:"80%",onClose:sa},{default:E((()=>[r(D,{ref:ea},null,512)])),_:1},512)]))}});$.__scopeId="data-v-0806278c";export default $;
