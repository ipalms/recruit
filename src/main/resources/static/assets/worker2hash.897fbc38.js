import{c as t}from"./vendor.db243dc8.js";let a=null,n=!1,s=0;self.onmessage=async e=>{const o=e.data;"boolean"==typeof o?n=o:async function(e,o){a||(a=await t(),a.init());const c=e.length;for(;s<c;){if(n)return;a.update(new Uint8Array(e[s])),postMessage(s+1),s++}const d=a.digest();a=null,s=0,postMessage([d,o.data],[...o.data])}(o,e)};