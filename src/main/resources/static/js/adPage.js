/* 待處理
  1. 分頁重複觸發的問題要修掉
*/

// 追蹤加入購物車的的項目
const cartItems = new Set();
// 查看購物車
const cart = document.getElementById("cart");
const modalBackground = document.getElementById("modal-background");
document.getElementById("check-cart-btn").addEventListener("click", async function () {
  modalBackground.classList.remove("hidden");
  cart.classList.remove('hidden');
  cart.classList.remove('hidden-right');
  cart.classList.remove('slide-out-right');
  cart.classList.add('slide-in-right');

  // 取得購物車內容
  const response = await fetch("/api/cart/list",{
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(conditions),
  });
  const cartItems = await response.json();

  console.log("cart items: ", cartItems);

  let cartLisElement = document.getElementById("cart-list");

  cartLisElement.innerHTML = "";
  cartItems.forEach((cartItem) =>{
    cartLisElement.innerHTML += generateCartItemRow(cartItem);
  });

  document.getElementById('submit-btn').addEventListener('click', function() {
    // 使用querySelector選取被選中的radio按鈕
    const selectedPaymentMethod = document.querySelector('input[name="payment-method"]:checked');
  
    if (selectedPaymentMethod) {
      console.log('選擇的付款方式是：', selectedPaymentMethod.value);

      const purchase = {
        "paymentMethod" : selectedPaymentMethod.value,
        "cartId" : cartItems[0].cartId,
      };

      console.log("purchase: ", purchase);

      // 攜帶參數前往結帳畫面
      // 將付款方式 + 購物車內容資料送到後端 > 呼叫API並轉址
      fetch("/orders/confirm",{
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(purchase),
      })
      .then((response) =>{
        return response.json();
      })
      .then((data) =>{
        console.log("定向回傳: ", data);
        if(data.redirectURL){
          window.location.href = data.redirectURL;
        }else{
          console.log("沒有重新定向的URL");
        }
      })
      .catch((error) =>{
        console.log("error: ", error);
      });

    }

  });

});

const closeCartButton = document.getElementById("close-cart");
closeCartButton.addEventListener("click", function() {
  cart.classList.add("slide-out-right");
  modalBackground.classList.add("hidden");
  
  setTimeout(function() {
    cart.classList.add("hidden");
    cart.classList.add("hidden-right");
  }, 500);
});

function generateCartItemRow(cartItem){

  const time = cartItem.addedDate.split("T");

  const cartItemHTML =
  `<div class="p-4 border rounded-lg shadow-sm">
    <div class="flex justify-between">
      <p class="font-semibold">
        廣告編號：<span class="text-blue-600">${cartItem.adId}</span>
      </p>
      <p class="text-gray-500">價格：NTD ${cartItem.adPrice}</p>
    </div>
    <p>廣告方案：<span class="text-green-600">${cartItem.adtypeId}</span></p>
    <p class="text-sm text-gray-500">
      加入購物車時間：${time[0]} ${time[1].substring(0,5)}
    </p>
  </div>`;

  return cartItemHTML;
}

// 設定篩選條件
const conditions = {
  page: 1,
  daterange: null,
  paymentstatus: null,
  input: null,
};

initTable(); // 初始化

// 初始化表格資料
function initTable() {
  filterAds();
}

// 監聽篩選條件的變化
const filters = document.querySelectorAll(".condition-filter");

filters.forEach((filter) => {
  filter.addEventListener("change", function () {
    conditions[filter.name] = filter.value;
    conditions.page = 1;
    filterAds();
  });
});

/*
const flatpickrInstance = flatpickr("#time-picker", {
  mode: "range",
  time_24hr: true,
  dateFormat: "Y-m-d",
  locale: { rangeSeparator: "~" },
  onChange: function (selectedDates) {
    if (selectedDates.length === 2) {
      const [startDate, endDate] = selectedDates;
      conditions.daterange = `${startDate.toISOString()} to ${endDate.toISOString()}`;
      console.log("Selected range:", conditions.daterange);
    }
  },
});
*/

// 及時搜尋及延後執行
const userInput = document.getElementById("user-input");
let debounceTimeout;

userInput.addEventListener("input", function () {
  clearTimeout(debounceTimeout); // 清除上次計時器
  debounceTimeout = setTimeout(() => {
    conditions.page = 1;
    conditions.input = userInput.value;
    filterAds();
  }, 300);
});

// 篩選廣告及生成內容
async function filterAds() {
  console.log("filter conditions: ", conditions);

  const cartResponse = await fetch("/api/cart/ads");
  const addedAdIds = await cartResponse.json();
  cartItems.clear();
  addedAdIds.forEach((adId) =>{
  cartItems.add(adId);
  })
  console.log("added cart items: ", cartItems);

  const response = await fetch("/advertisements/filter", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(conditions),
  });

  const adPages = await response.json();
  const tbody = document.querySelector("#ads-table-box tbody");

  tbody.innerHTML = "";
  adPages.content.forEach((ad) => {
    tbody.innerHTML += generateAdRow(ad);
  });

  updatePagination(
    adPages.totalPages,
    conditions.page,
    filterAds,
    "ads-table-box"
  );

  const editButtons = document.querySelectorAll(
    "#ads-table-box tbody .check-btn"
  );
  editButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const adId = this.closest("tr").getAttribute("data-ad-id");
      openEditForm(adId);
    });
  });

  const deleteBtns = document.querySelectorAll(
    "#ads-table-box tbody .delete-btn"
  );
  deleteBtns.forEach((button) => {
    button.addEventListener("click", function () {
      const adId = this.closest("tr").getAttribute("data-ad-id");
      const userConfirmed = window.confirm("確定要刪除嗎？");

      if (userConfirmed) {
        deleteAd(adId);
      }
    });
  });

  const addToCartBtns = document.querySelectorAll(".add-to-cart-btn");
  addToCartBtns.forEach((button) => {
    button.addEventListener("click", function () {
      const adId = button.closest("tr").getAttribute("data-ad-id");

      if (cartItems.has(adId)) {
        alert("該商品已加入購物車！");
        return;
      }

      // 送新增post到後端
      addItemToCart(adId).then((isSuccess) => {
        console.log(isSuccess);
        if (isSuccess) {
          cartItems.add(adId);
          this.disabled = true;
          this.textContent = "已加入";
        } else {
          alert("加入失敗，請重新操作");
        }
      });
    });
  });
}

async function addItemToCart(adId) {
  const response = await fetch("/api/cart/additem", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(adId),
  });

  const result = await response.json();

  return result;
}

async function openEditForm(adId) {
  toggleAdDetailModal();

  const response = await fetch(
    `/advertisements/adId/${adId}`
  );

  const adDetail = await response.json();

  const tbodyElement = document.querySelector("#ad-detail-modal tbody");
  const editButton = document.getElementById("edit-ad-btn");
  let isEditing = false;

  tbodyElement.innerHTML = "";

  let paidDate;
  let validation;
  let orderId;
  if (adDetail.paidDate === "尚未發布") {
    paidDate = "無";
    validation = "無";
    orderId = "無";
  } else {
    paidDate = adDetail.paidDate;
    validation = getAdValidation(adDetail.paidDate, adDetail.adName);
    orderId = adDetail.orderId;
  }

  const tbodyHTML = `<tbody>
    <tr>
      <th class="bg-gray-200 w-1/4 text-center">廣告編號</th>
      <td class="px-8 py-2">${adDetail.adId}</td>
    </tr>
    <tr>
      <th class="bg-gray-200 w-1/4 text-center">房屋標題</th>
      <td class="px-8 py-2">${adDetail.houseTitle}</td>
    </tr>
    <tr>
      <th class="adtype bg-gray-200 w-1/4">廣告方案</th>
      <td class="ad-plan-cell px-8 py-2">${adDetail.adName}</td>
    </tr>
    <tr>
      <th class="bg-gray-200 w-1/4">廣告金額</th>
      <td class="ad-price-cell px-8 py-2">NTD ${adDetail.adPrice}</td>
    </tr>
    <tr>
      <th class="bg-gray-200 w-1/4">付款狀態</th>
      <td class="px-8 py-2">${adDetail.isPaid}</td>
    </tr>
    <tr>
      <th class="bg-gray-200 w-1/4">訂單號碼</th>
      <td class="px-8 py-2">${orderId}</td>
    </tr>
    <tr>
      <th class="bg-gray-200 w-1/4">付款日期</th>
      <td class="px-8 py-2">${paidDate}</td>
    </tr>
    <tr>
      <th class="bg-gray-200 w-1/4">起訖日期</th>
      <td class="px-8 py-2">${validation}</td>
    </tr>
    <tr>
      <th class="bg-gray-200 w-1/4">推播成效</th>
      <td class="px-8 py-2">${"新增視覺化圖表"}</td>
    </tr>
  </tbody>`;

  tbodyElement.innerHTML += tbodyHTML;

  const adtypesResponse = await fetch(
    `/advertisements/adtypes`
  );
  const adtypes = await adtypesResponse.json();

  editButton.addEventListener("click", () => {
    if (!isEditing) {
      const adPlanCell = document.querySelector(".ad-plan-cell");

      const originalAdTypeName = adPlanCell.textContent.trim();

      const selectHTML = `
  <select id="ad-type-select" class="border border-gray-300 rounded px-2 py-1">
    ${adtypes
      .map(
        (adtype) =>
          `<option value="${adtype.adTypeId}" ${
            originalAdTypeName === adtype.adName ? "selected" : ""
          }>${adtype.adName}</option>`
      )
      .join("")}
      </select>
      <button id="save-ad-plan" class="px-2 py-1 bg-green-500 text-white rounded ml-2">儲存</button>
      <button id="cancel-ad-plan" class="px-2 py-1 bg-gray-500 text-white rounded ml-2">取消</button>
    `;

      // 更新 HTML
      adPlanCell.innerHTML = selectHTML;

      const adTypeSelect = document.getElementById("ad-type-select");

      const adPriceCell = document.querySelector(".ad-price-cell");

      adTypeSelect.addEventListener("change", () => {
        const selectedAdType = adTypeSelect.value;

        const selectedAd = adtypes.find(
          (adtype) => adtype.adTypeId == selectedAdType
        );

        if (selectedAd) {
          adPriceCell.textContent = `NTD ${selectedAd.adPrice}`;
        }
      });

      document
        .getElementById("save-ad-plan")
        .addEventListener("click", saveAdPlan);
      document
        .getElementById("cancel-ad-plan")
        .addEventListener("click", cancelEdit);

      isEditing = true;
    }

    async function saveAdPlan() {
      const selectedValue = document.getElementById("ad-type-select").value;
      const adPlanCell = document.querySelector(".ad-plan-cell");

      const response = await fetch("/advertisements", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          adId: adDetail.adId,
          newAdtypeId: selectedValue,
        }),
      });

      const updatedData = await response.json();

      const selectedAdType = adtypes.find(
        (adtype) => adtype.adTypeId == selectedValue
      );

      adPlanCell.textContent = selectedAdType.adName;

      isEditing = false;
    }

    function cancelEdit() {
      const adPlanCell = document.querySelector(".ad-plan-cell");

      // 恢復原始靜態文字
      adPlanCell.textContent = adDetail.adName;

      isEditing = false;
    }
  });
}

async function deleteAd(adId) {
  const response = await fetch("/advertisements", {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(adId),
  });

  const result = await response.json();

  if (result) {
    const viewAdButton = document.getElementById("view-ad-button");

    if (viewAdButton) {
      viewAdButton.remove();
    }

    const successMessageElement = document.getElementById("success-message");

    function updateSuccessMessage(newMessage) {
      successMessageElement.innerHTML = newMessage;
    }

    updateSuccessMessage(
      `<i class="fa-solid fa-check" style="color: #63e6be"></i> 刪除成功`
    );

    toggleSucessModal();
  }

  // conditions.page=1;
  filterAds();
}

function getAdValidation(startDateStr, adtype) {
  const totalDays = parseInt(adtype);

  const startDate = new Date(startDateStr);

  let endDate = new Date(startDate);
  endDate.setDate(startDate.getDate() + totalDays);

  const formatDate = (date) => date.toLocaleDateString("en-CA");

  const formattedStartDate = formatDate(startDate);
  const formattedEndDate = formatDate(endDate);

  const validation = `${formattedStartDate} ~ ${formattedEndDate}`;

  return validation;
}

function toggleAdDetailModal() {
  const adDetailModal = document.getElementById("ad-detail-modal");
  adDetailModal.classList.toggle("hidden");
}

document
  .getElementById("ad-detail-modal")
  .addEventListener("click", function (event) {
    if (event.target.id === "close-ad-modal-btn") {
      toggleAdDetailModal();
    }
  });

function calculateRemainingDays(startDateStr, adDays) {
  const totalDays = parseInt(adDays);

  const today = new Date();

  const startDate = new Date(startDateStr);

  const settlementDate = new Date(startDate);
  settlementDate.setDate(startDate.getDate() + totalDays);

  const timeDiff = settlementDate - today;

  const remainingDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

  return remainingDays;
}

function generateAdRow(ad) {

  const isAdded = cartItems.has(ad.adId);

  let adStatusSpanElement;
  let remainingDays = calculateRemainingDays(ad.paidDate, ad.adName);
  let deleteBtn;
  let cart;

  if (ad.isPaid === "已付款") {
    adStatusSpanElement = `<span class="inline-block px-2 py-1 text-xs font-medium rounded-full bg-yellow-100 text-yellow-800">推廣中</span>`;
    deleteBtn = `<button class="px-3 py-1 text-sm text-gray-400 border rounded" style="cursor: default;" disabled>刪除</button>`;
    cart = `<button class="px-3 py-1 text-sm text-gray-400 border rounded" style="cursor: default;" disabled>加入訂單</button>`;
  }

  if (ad.isPaid === "已付款" && remainingDays < 0) {
    adStatusSpanElement = `<span class="inline-block px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-800">已到期</span>`;
    remainingDays = "無";
    deleteBtn = `<button class="px-3 py-1 text-sm text-gray-400 border rounded" style="cursor: default;" disabled>刪除</button>`;
    cart = `<button class="px-3 py-1 text-sm text-gray-400 border rounded" style="cursor: default;" disabled>加入訂單</button>`;
  }

  if (ad.isPaid === "未付款") {
    adStatusSpanElement = `<span class="inline-block px-2 py-1 text-xs font-medium rounded-full bg-pink-100 text-pink-800">未發布</span>`;
    remainingDays = "無";
    deleteBtn = `<button class="delete-btn px-3 py-1 text-sm text-red-600 bg-red-100 rounded hover:bg-red-200">刪除</button>`;
    if(isAdded){
      cart = `<button class="add-to-cart-btn px-3 py-1 text-sm text-gray-600 bg-gray-100 rounded" style="cursor: default;" disabled>已加入</button>`;
    }else{
      cart = `<button class="add-to-cart-btn px-3 py-1 text-sm text-green-600 bg-green-100 rounded hover:bg-green-200">加入訂單</button>`;
    }
  }

  return `
    <tr class="border-b border-gray-200 hover:bg-gray-50" data-ad-id="${ad.adId}">
      <td class="px-4 py-3 text-sm text-gray-700 text-center">${ad.houseTitle}</td>
      <td class="px-4 py-3 text-sm text-center text-gray-700">${ad.paidDate}</td>
      <td class="px-4 py-3 text-sm text-center">
        ${adStatusSpanElement}</td>
      <td class="px-4 py-3 text-sm text-center text-gray-700">${ad.isPaid}</td>
      <td class="px-4 py-3 text-sm text-center text-gray-700">${remainingDays}</td>
      <td class="px-4 py-3 text-center">
        <button class="check-btn px-3 py-1 text-sm text-blue-600 bg-blue-100 rounded hover:bg-blue-200">查看與編輯</button>
        ${deleteBtn}
        ${cart}
      </td>
    </tr>`;
}

// 設置分頁
// let paginationInitialized = false; // 用來避免重複初始化
const maxVisiblePages = 10;

function updatePagination(
  totalPages,
  currentPage,
  filterCallback,
  boxElementId
) {
  const paginationElement = document.querySelector(`#${boxElementId} ul`);

  paginationElement.innerHTML = ""; // 清空現有的分頁按鈕

  const prevButton = createPaginationButton("prev-page", "&lt;");
  paginationElement.appendChild(prevButton);

  const startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
  const endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

  for (let i = startPage; i <= endPage; i++) {
    const pageButton = createPaginationButton(i, i);
    if (i === currentPage) {
      pageButton.classList.add("bg-blue-500", "rounded", "text-white");
    } else {
      pageButton.classList.add("bg-white", "text-gray-600");
    }
    paginationElement.appendChild(pageButton);
  }

  const nextButton = createPaginationButton("next-page", "&gt;");
  paginationElement.appendChild(nextButton);

  setupPagination(totalPages, filterCallback, boxElementId);
}

// 建立分頁按鈕
function createPaginationButton(page, label) {
  const button = document.createElement("li");
  button.innerHTML = `
    <button data-page="${page}" class="px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-400">${label}</button>
  `;
  return button;
}

function setupPagination(totalPages, filterCallback, boxElementId) {
  // if (paginationInitialized) return;

  const paginationElement = document.querySelector(
    `#${boxElementId} .pagination`
  );

  paginationElement.addEventListener("click", handlePaginationClick);

  function handlePaginationClick(event) {
    const page = event.target.getAttribute("data-page");

    if (!page) return;

    if (page === "prev-page" && conditions.page > 1) {
      conditions.page--;
    } else if (page === "next-page" && conditions.page < totalPages) {
      conditions.page++;
    } else if (page !== "prev-page" && page !== "next-page") {
      conditions.page = parseInt(page, 10);
    } else {
      conditions.page = totalPages;
    }

    filterCallback();
  }

  // paginationInitialized = true; // 標記為已初始化
}

// 新增廣告及關閉
const addAdBtn = document.getElementById("add-ad-btn");
addAdBtn.addEventListener("click", function () {
  document.getElementById("ads-table-box").classList.add("hidden");
  document.getElementById("ad-add-table-box").classList.remove("hidden");

  const successMessageElement = document.getElementById("success-message");

  function updateSuccessMessage(newMessage) {
    successMessageElement.innerHTML = newMessage;

    let viewAdButton = document.getElementById("view-ad-button");
    const successModal = document.getElementById("success-modal");

    if (!viewAdButton) {
      viewAdButton = document.createElement("button");
      viewAdButton.textContent = "前往查看";
      viewAdButton.classList.add(
        "px-3",
        "py-1.5",
        "bg-blue-500",
        "text-white",
        "rounded",
        "hover:bg-blue-600",
        "focus:outline-none",
        "focus:ring-2",
        "focus:ring-blue-400"
      );

      viewAdButton.id = "view-ad-button";

      const closeButtonContainer = successModal.querySelector(".text-right");
      closeButtonContainer.insertBefore(
        viewAdButton,
        closeButtonContainer.firstChild
      );
    }

    viewAdButton.addEventListener("click", function () {
      successModal.classList.add("hidden");
      document.getElementById("ad-add-table-box").classList.add("hidden");
      document.getElementById("ads-table-box").classList.remove("hidden");

      filterAds();
    });
  }

  updateSuccessMessage(
    '<i class="fa-solid fa-check" style="color: #63e6be"></i> 新增成功'
  );

  filterHousesWithoutAds();
});

// 如果是從order頁面跳過來
window.onload = function () {
  const params = new URLSearchParams(window.location.search);
  const action = params.get("action");

  if (action === "addAd") {
    if (addAdBtn) {
      addAdBtn.click();
    } else {
      console.log("按鈕不存在");
    }
  }
};

closeAddTable();

// 關閉新增廣告表格
function closeAddTable() {
  document.getElementById("close-add").addEventListener("click", function () {
    document.getElementById("ads-table-box").classList.remove("hidden");
    document.getElementById("ad-add-table-box").classList.add("hidden");
    filterAds();
  });
}

// 用會員id搜尋沒有廣告的房子
async function filterHousesWithoutAds() {
  const pageNumber = conditions.page;
  const response = await fetch(
    `/advertisements/houseswithoutadds/${pageNumber}`
  );
  const data = await response.json();

  const adtypesResponse = await fetch(
    `/advertisements/adtypes`
  );
  const adtypes = await adtypesResponse.json();

  showHouses(data.content, adtypes);

  updatePagination(
    data.totalPages,
    pageNumber,
    filterHousesWithoutAds,
    "ad-add-table-box"
  );
}

function showHouses(houseContents, adtypes) {
  const closeAddButton = document.getElementById("close-add");
  const tbody = document.querySelector("#ad-add-table-box tbody");
  const closeAddRow = closeAddButton.closest("tr");

  while (tbody.firstChild) {
    if (tbody.firstChild !== closeAddRow) {
      tbody.removeChild(tbody.firstChild);
    } else {
      break;
    }
  }

  for (let i = 0; i < houseContents.length; i++) {
    const selectElement = document.createElement("select");

    adtypes.forEach((adtype) => {
      const option = document.createElement("option");
      option.value = adtype.adTypeId;
      option.textContent = adtype.adName;
      selectElement.appendChild(option);
    });

    let adPrice = adtypes[0].adPrice;
    selectElement.addEventListener("change", function () {
      const selectedValue = selectElement.value;
      const selectedAdType = adtypes.find((ad) => ad.adTypeId == selectedValue);

      if (selectedAdType) {
        adPrice = selectedAdType.adPrice;
        updatePriceAndAttribute(adPrice, selectedAdType.adTypeId);
      }

      function updatePriceAndAttribute(price, adTypeId) {
        const priceElement = newRow.querySelector(".ad-price");
        priceElement.textContent = `NTD ${price}`;
        priceElement.setAttribute("data-adtype-id", adTypeId);
      }
    });

    const newRow = document.createElement("tr");
    newRow.classList.add("border-b", "border-gray-200", "hover:bg-gray-100");

    newRow.innerHTML = `
    <td class="px-4 py-3" data-house-id="${houseContents[i].houseId}">${houseContents[i].houseTitle}</td>
    <td class="px-4 py-3">
      <button class="px-3 py-1 text-sm text-blue-600 bg-blue-100 rounded hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-blue-400">查看</button>
    </td>
    <td class="px-4 py-3">
    </td>
    <td class="px-4 py-3 text-right ad-price" data-adtype-id="${adtypes[0].adTypeId}">
      NTD ${adPrice}</td>
    <td class="px-4 py-3 text-center">
      <button class="add px-4 py-2 text-white bg-green-500 rounded-lg hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400">新增</button>
    </td>`;

    newRow.querySelector("td:nth-child(3)").appendChild(selectElement);

    const addButton = newRow.querySelector("button.bg-green-500");
    addButton.addEventListener("click", function () {
      const houseId = newRow
        .querySelector("td[data-house-id]")
        .getAttribute("data-house-id");
      const adTypeId = newRow
        .querySelector("td[data-adtype-id]")
        .getAttribute("data-adtype-id");

      addSelectedAd(houseId, adTypeId);
    });

    tbody.insertBefore(newRow, closeAddButton.closest("tr"));
  }
}

async function addSelectedAd(huoseId, adTypeId) {
  const response = fetch("/advertisements", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      houseId: huoseId,
      adtypeId: adTypeId,
    }),
  });

  const result = await response.json();

  if (result) {
    toggleSucessModal();
  }

  filterHousesWithoutAds();
}

const sucessModal = document.getElementById("success-modal");
sucessModal.addEventListener("click", function (event) {
  if (event.target.id === "close-sucess-modal") {
    toggleSucessModal();
  }
});

function toggleSucessModal() {
  // const backModal = document.getElementById("modal-background");
  const sucessModal = document.getElementById("success-modal");
  // backModal.classList.toggle("hidden");
  sucessModal.classList.toggle("hidden");
}
