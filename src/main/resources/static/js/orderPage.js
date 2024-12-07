
/* 初始化 table */
initDataTable();

function initDataTable(){
  filterOrders({
    "status": "allOrders",
    "condition": null,
    "input": null
  });
}


/* 上排搜尋條件的按鈕 */
const conditionBtnsGroup1 = Array.from(document.querySelectorAll(".condition-btn-group1"));
const conditionBtnsGroup2 = Array.from(document.querySelectorAll(".condition-btn-group2"));
const dropdownMenuItems = Array.from(document.querySelectorAll(".menu-item"));
const menuBtn = document.getElementById("menu-button");


/* 使用者輸入搜尋條件的輸入框 */
const searchInput = document.getElementById("search-input");

/* 處理下單選單 - 以其他方式搜尋 */
const dropdownButton = document.getElementById("menu-button");
const dropdownMenu = document.getElementById("dropdown-menu");

dropdownButton.addEventListener("click", function (event) {
  event.stopPropagation();
  dropdownMenu.classList.toggle("hidden");
});

document.addEventListener("click", function (event) {
  if (
    !dropdownButton.contains(event.target) &&
    event.target != dropdownButton
  ) {
    dropdownMenu.classList.add("hidden");
  }
});

const selectedConditions = {
  status: "allAds",
  condition: null,
  input: null,
}

// 初始化監聽事件
initListeners();

function initListeners(){
  initBtnListeners(conditionBtnsGroup1, "group1");
  initBtnListeners(conditionBtnsGroup2, "group2");
  initDropMenuListeners(dropdownMenuItems);
  initSearchInputLinstener();
}

function initBtnListeners(buttons, group){
  buttons.forEach((button) =>{
    button.addEventListener("click", function(){
      handleButtonSelection(this, group);
      applyFilters();
    });
  });
}

function initDropMenuListeners(menuItems){
  menuItems.forEach((item) =>{
    item.addEventListener("click", function(){
      const searchCondition = item.value;
      handleDropdownSelection(searchCondition);
    });
  });
}

function initSearchInputLinstener(){
  searchInput.addEventListener("input", function(event){

    const input = event.target.value;
    selectedConditions.input = input;
    applyFilters();

    // if(event.key === "Enter"){
    // }

  })
}

function handleButtonSelection(button, group){
  let buttons = group === "group1"? conditionBtnsGroup1 : conditionBtnsGroup2;

  removeBtnColor(buttons);

  if(group === "group1"){
    selectedConditions.status = button.value;
  }
  
  button.classList.add("bg-blue-700");
}

// 按鈕除色
function removeBtnColor(buttons) {
  buttons.forEach((button) => {
    button.classList.remove("bg-blue-700");
    button.classList.add("bg-blue-500");
  });
}

function handleDropdownSelection(condition){
  showInputElements(condition);
  selectedConditions.condition = condition;
}


function applyFilters(){
  // console.log("filter: ", selectedConditions);

  filterOrders(selectedConditions);
}

// 取得所有訂單的資料
// function getAllOrders() {

//   // 測試資料
//   let pageNumber = 1;

//     fetch(
//       `http://localhost:8080/api/orders/${pageNumber}`,
//       { 
//         method: "GET",
//       }
//     ).then((response) => {
//       return response.json();
//     }).then((orders) =>{

//       /* {userId, merchantId, merchantTradNo, merchantTradDate, totalAmount, tradeDesc, itemName, orderStatus, returnUrl, choosePayment, checkMacValue} */

//       if(orders.length === 0){
//         const tbody = document.querySelector("#paid-list-table tbody");
//         tbody.innerHTML = "";
//         row = 
//           `<tbody>
//             <tr>
//               <td class="px-4 py-2">查無資料</td>
//             </tr>
//           </tbody>`;
//           tbody.innerHTML += row;

//         return;
//       }

//       const tbody = document.querySelector("#paid-list-table tbody");
//       tbody.innerHTML = "";

//       for (let i = 0; i < orders.length; i++) {

//         // 之後放去DTO計算
//         const orderDate = orders[0].merchantTradDate.substring(0,10);
//         const orderStatus = orders[0].orderStatus === 0? "已取消" : "一般訂單";

//         row = 
//         `<tbody>
//           <tr>
//             <td class="px-4 py-2">${orders[i].merchantTradNo}</td>
//             <td class="px-4 py-2">
//               <ul>
//                 <li>動態新增訂單內容物</li>
//                 <li>至多兩物件</li>
//               </ul>
//             </td>
//             <td class="px-4 py-2">${orderDate}</td>
//             <td class="px-4 py-2">${orderStatus}</td>
//             <td class="px-4 py-2 text-center">
//               <button
//                   type="button"
//                   class="check-detail-btn bg-blue-500 rounded text-white w-16 py-2 hover:bg-blue-300">
//                   查看
//               </button></td>
//           </tr>
//         </tbody>`;
//         tbody.innerHTML += row;
//       }
//     })
//     .catch((error) =>{
//       console.log(error);
//     });
// }

// 以訂單狀態搜尋
function filterOrders(selectedConditions){

  console.log(selectedConditions);

  // 測試資料 
  let pageNumber = 1
  
  const selectedConditionsJson = JSON.stringify(selectedConditions);

  fetch(`http://localhost:8080/api/orders/filter/${pageNumber}`,{
    method: "POST",
    headers: {
      'Content-Type': 'application/json',
    },
    body: selectedConditionsJson,
  })
  .then((response) =>{
    return response.json();
  })
  .then((orders) =>{
    // console.log(orders);

    const tbody = document.querySelector("#paid-list-table tbody");
    tbody.innerHTML = "";

    if(orders.length === 0){
      const div = document.createElement("div");
      div.classList.add("px-4", "py-2");
      div.textContent = "查無資料";

      tbody.appendChild(div);

      return;
    }

    for (let i = 0; i < orders.length; i++) {
      const merchantTradDate = orders[i].merchantTradDate.substring(0,10);
      row = 
      `<tbody>
        <tr class="border-b border-gray-200 hover:bg-gray-50">
          <!-- 廣告編號 -->
          <td class="px-4 py-3 text-sm font-medium text-gray-700">
            ${orders[i].merchantTradNo}
          </td>

          <!-- 房屋標題 -->
          <td class="px-4 py-3 text-sm text-gray-700">
            <ul class="list-disc pl-4">
              <li>${orders[i].houseTitles}</li>
            </ul>
          </td>

          <!-- 訂單日期 -->
          <td class="px-4 py-3 text-sm text-gray-700">
            ${merchantTradDate}
          </td>

          <!-- 訂單狀態 -->
          <td class="px-4 py-3 text-sm text-gray-700">
            <span class="inline-block px-2 py-1 text-xs font-medium rounded-full
              ${orders[i].orderStatus === '已完成' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'}">
              ${orders[i].orderStatus}
            </span>
          </td>

          <td class="px-4 py-3 text-center">
            <button
              type="button"
              class="check-detail-btn bg-blue-600 text-white text-sm font-semibold py-2 px-4 rounded-lg hover:bg-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-opacity-75">
              查看
            </button>
          </td>
        </tr>
      </tbody>`;
      tbody.innerHTML += row;
    }
    
  })
  .catch((error) =>{
    console.log(error);
  });
  
}


/* 下拉選單選項 */
function showInputElements(condition){

  if (searchInput.flatpickrInstance) {
    searchInput.flatpickrInstance.destroy();
    delete searchInput.flatpickrInstance;
  }

  if(condition === "merchantTradNo"){
    menuBtn.innerHTML =
    '以訂單號碼搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';

    searchInput.classList.remove("hidden");

    searchInput.value = "";
    searchInput.placeholder = "請輸入訂單編號";
  }

  if(condition === "period"){
    menuBtn.innerHTML =
    '以時間搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';

    searchInput.classList.remove("hidden");

    searchInput.value = "";
    searchInput.id = "date-input";

    searchInput.placeholder = "請選擇想查詢的時間區段";

    const flatpickrInstance = flatpickr("#date-input", {
      mode: "range",
      time_24hr: true,
      dateFormat: "Y-m-d",
      onChange: function (period) {
        if (period.length === 2) {
          const startDate = period[0];
          const endDate = period[1];
          const searchInputValue = `${startDate.toISOString()} to ${endDate.toISOString()}`;
          console.log("Selected range:", searchInputValue);
        }
      },
    });

    searchInput.flatpickrInstance = flatpickrInstance;

  }

  if(condition === "houseTitle"){
    menuBtn.innerHTML =
    '以房屋標題搜尋 <i class="fa-solid fa-arrow-down py-1"></i>';

    searchInput.classList.remove("hidden");

    searchInput.value = "";
    searchInput.placeholder = "請輸入房屋標題";
  }

  if(condition === "none"){
    menuBtn.innerHTML =
    '不設條件 <i class="fa-solid fa-arrow-down py-1"></i>';
    searchInput.classList.add("hidden");
  }
}



/* 訂單內容 modal */
const tbody = document.querySelector("#paid-list-table tbody");
const modal = document.getElementById("modal");
const modalBackground = document.getElementById("modal-background");

tbody.addEventListener("click", function (event) {
  const checkBtn = event.target;
  if(event.target.classList.contains("check-detail-btn")){
    const metchantTradNo = checkBtn.closest("tr").children[0].textContent;
    console.log(metchantTradNo);

    // 取的訂單及廣告資料
    fetchOrderAndAdDetails(metchantTradNo)
    .then(([adList, orderDetail]) => {
      console.log("adList:", adList);
      console.log("orderDetail:", orderDetail);

      const orderDate = orderDetail.merchantTradDate.substring(0,10);

      // 顯示 modal
      modal.classList.remove("hidden");

      // 變更 modal 內容
      const orderModalTable = document.getElementById("order-modal-table");
      orderModalTable.innerHTML = "";

      const content = 
      `
        <table id="order-modal-table" class="border border-gray-400 w-full">
        <thead class="border border-gray-400">
          <tr>
            <th class="bg-gray-200 w-1/4">訂單編號</th>
            <td colspan="4" class="px-2">${orderDetail.merchantTradNo}</td>
          </tr>
          <tr>
            <th class="bg-gray-200 w-1/4">下單時間</th>
            <td colspan="4" class="px-2">${orderDate}</td>
          </tr>
          <tr>
            <th class="bg-gray-200 w-1/4">訂單狀態</th>
            <td colspan="4" class="px-2">${orderDetail.orderStatus}</td>
          </tr>
        </thead>
        <tbody id="ad-details-body" class="text-center">
          <tr>
            <th class="bg-gray-200 w-1/4">物件內容</th>
            <th class="bg-gray-200 w-1/4">廣告種類</th>
            <th class="bg-gray-200 w-1/4">廣告金額</th>
            <th class="bg-gray-200 w-1/4">廣告詳細</th>
          </tr>
        </tbody>
      </table>`;

      orderModalTable.innerHTML += content;


      const tbody = document.getElementById("ad-details-body");

      // 動態加入ads資料
      adList.forEach((ad) =>{
        const tr = document.createElement("tr");

        // const adIdTd = document.createElement("td");adIdTd.textContent = ad.adId;
        const houseTitleTd = document.createElement("td");
        houseTitleTd.textContent = ad.houseTitle;
        const adNameTd = document.createElement("td");
        adNameTd.textContent = ad.adName;
        const adPriceTd = document.createElement("td");
        adPriceTd.textContent = ad.adPrice;
        const adDetails = document.createElement("td");
        adDetails.innerHTML = 
        `<button
          type="button"
          class="bg-yellow-500 rounded text-white w-16 py-1 hover:bg-yellow-300">
          查看
        </button>`;

        tr.appendChild(houseTitleTd);
        tr.appendChild(adNameTd);
        tr.appendChild(adPriceTd);
        tr.appendChild(adDetails);

        tbody.appendChild(tr);
      });

      const totalAmountContent =
      `<tr>
        <th class="w-1/4 px-4 py-2"></th>
        <th class="w-1/4 px-4 py-2">訂單金額</th>
        <td class="w-1/4 px-4 py-2">${orderDetail.totalAmount}</td>
      </tr>`;

      tbody.innerHTML += totalAmountContent;

      // 點選廣告詳細的話可以看詳細內容

      toggleModalBackground();
    });
  }
});

modal.addEventListener("click", function (event) {
  if (event.target.id === "close-modal-btn") {
    modal.classList.add("hidden");
    toggleModalBackground();
  }
});

function toggleModalBackground() {
  modalBackground.classList.toggle("overlay");
  modalBackground.classList.toggle("hidden");
}

async function fetchOrderAndAdDetails(merchantTradNo){
  try{
    const [adList, orderDetail] = await Promise.all([
      fetch(`http://localhost:8080/advertisements/orderId/${merchantTradNo}`).then((response) => response.json()), 
      fetch(`http://localhost:8080/api/orders/${merchantTradNo}`).then((response) => response.json())
    ]);

    return [adList, orderDetail];

  }catch(error){
    console.log("error in one of the two requests");
  }
}

// 跳去新增廣告
// const addAdBtn = document.getElementById("add-btn");
// addAdBtn.addEventListener("click", function(){
//   fetch("http://localhost:8080/orders/mylist")
// });