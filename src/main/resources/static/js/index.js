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
CardJs.loadMoreCards(6);

//Google map Search
GoogleApiJs.googleSearchEventConfig();
GoogleApiJs.initMap();

//TEST event
// TestJs.testFetch();