import{ab as e}from"./index.77974a83.js";const a=(a,r,s=10)=>e.get("/favorite/queryFavorites",{params:{userId:a,page:r,rows:s}}),r=(a,r=10,s,t,i)=>e.get("/article/queryArticles",{params:{page:a,rows:r,userId:s,adminName:i,courseName:t}}),s=(a,r)=>e.get("/article/queryOneArticle",{params:{articleId:a,userId:r}}),t=(a,r)=>e.post("/like/changeLikeStatus",{userId:a,articleId:r}),i=(a,r)=>e.post("/favorite/changeFavoriteStatus",{userId:a,articleId:r});export{a,r as b,i as c,t as d,s as e};
