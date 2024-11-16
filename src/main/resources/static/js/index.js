import * as ListJs from './ListScript.js';
import * as ButtonJs from './ButtonScript.js';
import * as CardJs from './CardScript.js';
import * as GoogleApiJs from './GoogleScript.js';
import * as TestJs from './TestScript.js'

//List event
ListJs.listEventConfig();

//Button event
ButtonJs.buttonEventConfig();

//Card event
CardJs.cardEventConfig();
//Card 進入首頁時顯示的項目
CardJs.loadMoreCards(6);

//GoogleMap event
GoogleApiJs.googleSearchEventConfig();
//GoogleMap Initialize
GoogleApiJs.initMap();

//TEST event
// TestJs.testFetch();