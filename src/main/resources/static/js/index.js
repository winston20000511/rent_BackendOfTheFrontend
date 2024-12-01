import * as ListJs from './ListScript.js';
import * as ButtonJs from './ButtonScript.js';
import * as CardJs from './CardScript.js';
import * as GoogleApiJs from './GoogleScript.js';
import * as TestJs from './TestScript.js'


//List event
try {
    ListJs.init();
    TestJs.addEvent('.link','mouseenter',ListJs.linkMouseenter); //link滑鼠移入
    TestJs.addEvent('.link','mouseleave',ListJs.linkMouseleave); //link滑鼠移入
    TestJs.addEvent('.link-list','mouseenter',ListJs.listMouseenter); //list滑鼠移入
    TestJs.addEvent('.link-list','mouseleave',ListJs.listMouseleave); //list滑鼠移入
    console.log('List 初始化成功 ...');
} catch (error) {
    console.log('List 初始化失敗 ...',error)
}

//Button event
try{
    ButtonJs.init();
    TestJs.addEvent('#filter-btn-1','click',ButtonJs.buttonClick);
    console.log('Button 初始化成功 ...');
}catch(error){
    console.log('Button 初始化失敗 ...',error)
}


//ButtonJs.buttonEventConfig();

//Card event
CardJs.cardEventConfig();
//Card 進入首頁時顯示的項目
CardJs.loadMoreCards(6);

//GoogleMap event
GoogleApiJs.googleSearchEventConfig();
//GoogleMap Initialize
GoogleApiJs.initMap();

