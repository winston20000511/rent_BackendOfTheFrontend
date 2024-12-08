// 為物件申請VIP服務
const addAdBtn = document.getElementById("add-ad-btn");
addAdBtn.addEventListener("click", function () {
  window.location.href = "http://localhost:8080/advertisements/mylist?action=addAd";
});

// 設定篩選條件
const conditions = {
  page: 1,
  status: null,
  daterange: null,
  inputcondition: null,
  input: null,
};

initTable(); // 初始化

// 初始化表格資料
function initTable() {
  filterOrders();
}

// 監聽篩選條件的變化
const filters = document.querySelectorAll(".condition-filter");

filters.forEach((filter) => {
  filter.addEventListener("change", function (event) {
    const selectedEmelemt = event.target;
    if (selectedEmelemt.id === "filter-type") {
      let inputBox = document.getElementById("input-box");

      inputBox.classList.remove("hidden");
      const userInputElement = document.getElementById("user-input");
      userInputElement.value = "";
      if (selectedEmelemt.value === "orderid") {
        userInputElement.setAttribute("placeholder", "請輸入訂單編號");
      }

      if (selectedEmelemt.value === "housetitle") {
        userInputElement.setAttribute("placeholder", "請輸入房屋標題");
      }

      if (selectedEmelemt.value === "none") {
        userInputElement.removeAttribute("placeholder", "請輸入訂單編號");
        inputBox.classList.add("hidden");
      }
    }

    conditions[filter.name] = filter.value;
    conditions.page = 1;

    filterOrders();
  });
});

//即時搜尋及延後執行
const userInput = document.getElementById("user-input");
let debounceTimeout;

userInput.addEventListener("input", function () {
  clearTimeout(debounceTimeout); // 清除上次計時器
  debounceTimeout = setTimeout(() => {
    conditions.page = 1;
    conditions.input = userInput.value;
    filterOrders();
  }, 300);
});

// 篩選廣告及生成內容
async function filterOrders() {
  console.log("filter conditions: ", conditions);

  const response = await fetch("http://localhost:8080/api/orders/filter", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(conditions),
  });

  const orderPages = await response.json();

  const tbody = document.querySelector("#order-table tbody");

  tbody.innerHTML = "";
  orderPages.content.forEach((order) => {
    tbody.innerHTML += generateOrderRow(order);
  });

  updatePagination(orderPages.totalPages, conditions.page, filterOrders);

  // 查看詳細
  const checkButtons = document.querySelectorAll("#order-box tbody .check-btn");

  checkButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const merchantTradNo = this.closest("tr").firstElementChild.textContent;
      openCheckForm(merchantTradNo);
    });
  });

  // 取消訂單
  const cancelButtons = document.querySelectorAll("#order-box tbody .cancel-btn");
  cancelButtons.forEach((button) =>{
    button.addEventListener("click", function () {
      const merchantTradNo = this.closest("tr").firstElementChild.textContent;
      cancelOrder(merchantTradNo);
    });
  })

}

async function openCheckForm(merchantTradNo) {
  toggleOrderDetailModal();

  const response = await fetch(
    `http://localhost:8080/api/orders/merchantTradNo`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: merchantTradNo,
    }
  );

  const orderDetail = await response.json();

  setupcheckForm(orderDetail);
}

// 取消訂單
async function cancelOrder(merchantTradNo){
  const message = document.getElementById("message");
  const userConfirmed = window.confirm("確定要取消訂單嗎？");
  if(userConfirmed){
    message.textContent = "已提出取消申請";
    const hintMessage = document.createElement("div");
    hintMessage.classList.add("text-center");
    hintMessage.textContent = "請待服務人員確認，方能取消訂單";
    message.insertAdjacentElement("afterend", hintMessage);
    toggleMessageModal();

    const response = await fetch("http://localhost:8080/api/orders/merchantTradNo",{
      method:"PUT",
      headers: { "Content-Type": "application/json" },
      body: merchantTradNo,
    });

    const result = response.json();
    console.log(result);

    filterOrders();

  }else{
    message.textContent = "訂單未取消";
    toggleMessageModal();
  }
}

const closeButton = document.getElementById("close-modal");
closeButton.addEventListener("click", function(){
  toggleMessageModal();
});

function toggleMessageModal(){
  const messageModal = document.getElementById("message-modal");
  const modalBackground = document.getElementById("modal-background");
  messageModal.classList.toggle("hidden");
  modalBackground.classList.toggle("hidden");
}


function setupcheckForm(orderDetail){
  // table thead內容
  const theadElement = document.querySelector("#order-modal-table thead");
  theadElement.children[0].innerHTML = `<th class="bg-gray-100">訂單號碼</th>
   <td colspan="3" class="px-4 py-2">${orderDetail.merchantTradNo}`;
  theadElement.children[1].innerHTML = `<th class="bg-gray-100">付款日期</th>
    <td colspan="3" class="px-4 py-2">${orderDetail.merchantTradDate}`;
  theadElement.children[2].innerHTML = `<th class="bg-gray-100">訂單狀態</th>
    <td colspan="3" class="px-4 py-2">${orderDetail.orderStatus}`;
  theadElement.children[3].innerHTML = `<th class="bg-gray-100">訂單狀態</th>
    <td colspan="3" class="px-4 py-2">${orderDetail.choosePayment}`;

  // table tbody 內容
  const tbodyElement = document.getElementById("order-details-body");
  tbodyElement.innerHTML = "";

  const adTd = document.createElement("td");
  adTd.classList.add("py-2");
  const adtypeTd = document.createElement("td");
  const adPriceTd = document.createElement("td");
  const houseTitleTd = document.createElement("td");
  const voucherTd = document.createElement("td");
  const subtotalTd = document.createElement("td");
  for (let i = 0; i < orderDetail.adIds.length; i++) {
    // ad ids
    const adId = orderDetail.adIds[i];
    const adSpan = document.createElement("span");
    adSpan.textContent = adId;
    adSpan.style.display = "block";
    adTd.appendChild(adSpan);

    // ad type names
    const adtypeName = orderDetail.adtypes[i];
    const adtypeSpan = document.createElement("span");
    adtypeSpan.textContent = adtypeName;
    adtypeSpan.style.display = "block";
    adtypeTd.appendChild(adtypeSpan);

    // ad prices
    const adPrice = orderDetail.prices[i];
    const adPriceSpan = document.createElement("span");
    adPriceSpan.textContent = adPrice;
    adPriceSpan.style.display = "block";
    adPriceTd.appendChild(adPriceSpan);

    // house title
    const houseTitle = orderDetail.houseTitles[i];
    const houseTitleSpan = document.createElement("span");
    houseTitleSpan.textContent = houseTitle;
    houseTitleSpan.style.display = "block";
    houseTitleTd.appendChild(houseTitleSpan);

    // voucher
    const voucher = "voucher";
    const voucherSpan = document.createElement("span");
    voucherSpan.textContent = voucher;
    voucherSpan.style.display = "block";
    voucherTd.appendChild(voucherSpan);

    // subtotal
    const subtotal = "subtotal";
    const subtotalSpan = document.createElement("span");
    subtotalSpan.textContent = subtotal;
    subtotalSpan.style.display = "block";
    subtotalTd.appendChild(subtotalSpan);
  }

  const tbodyHTML = `<tbody>
    <tr>
      <th class="bg-gray-200 text-center">廣告編號</th>
      <th class="bg-gray-200 text-center">VIP方案</th>
      <th class="adtype bg-gray-200 text-center">方案金額</th>
      <th class="bg-gray-200 text-center">房屋標題</th>
      <th class="bg-gray-200 text-center">折扣</th>
      <th class="bg-gray-200 text-center">小計</th>
    </tr>
    <tr>
      ${adTd.outerHTML}
      ${adtypeTd.outerHTML}
      ${adPriceTd.outerHTML}
      ${houseTitleTd.outerHTML}
      ${voucherTd.outerHTML}
      ${subtotalTd.outerHTML}
    </tr>
    <tr>
      <td colspan="4"></td>
      <td class="py-1 text-center">總金額</td>
      <td><strong>${orderDetail.totalAmount}</strong></td>
    </tr>
  </tbody>`;

  tbodyElement.innerHTML += tbodyHTML;
}

function toggleOrderDetailModal() {
  const orderDetailModal = document.getElementById("order-detail-modal");
  const modalBackground = document.getElementById("modal-background");
  orderDetailModal.classList.toggle("hidden");
  modalBackground.classList.toggle("hidden");
}

document.getElementById("order-detail-modal").addEventListener("click", function (event) {
    if (event.target.id === "close-modal-btn") {
      toggleOrderDetailModal();
    }
  });

function generateOrderRow(order) {
  let orderSpanElement;
  let cancelButtonElement;

  if (order.orderStatus === "一般訂單") {
    orderSpanElement = `<span class="inline-block px-2 py-1 text-xs font-medium rounded-full bg-yellow-100 text-yellow-800">一般訂單</span>`;
    cancelButtonElement = `<button class="cancel-btn px-3 py-1 text-sm text-red-600 bg-red-100 rounded hover:bg-red-200">取消</button>`;
  }

  if (order.orderStatus === "已取消") {
    orderSpanElement = `<span class="inline-block px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-800">已取消</span>`;
    cancelButtonElement = `<button class="px-3 py-1 text-sm text-gray-400 border rounded" style="cursor: default;" disabled>取消</button>`;
  }

  if (order.orderStatus === "取消中") {
    orderSpanElement = `<span class="inline-block px-2 py-1 text-xs font-medium rounded-full bg-pink-100 text-pink-800">取消中</span>`;
    cancelButtonElement = `<button class="px-3 py-1 text-sm text-gray-400 border rounded" style="cursor: default;" disabled>取消</button>`;
  }

  let orderDetailsContent = `<ul class="text-center text-sm text-gray-700">`;
  for (let i = 0; i < order.adIds.length; i++) {
    const truncatedTitle =
      order.houseTitles[i].length > 10
        ? order.houseTitles[i].substring(0, 10) + "..."
        : order.houseTitles[i];
    orderDetailsContent += `<li><strong>．${truncatedTitle}</strong></li>`;
  }
  orderDetailsContent += `<ul>`;

  return `
    <tr class="border-b border-gray-200 hover:bg-gray-50">
      <td class="px-4 py-3 text-sm text-gray-700 text-center">${order.merchantTradNo}</td>
      <td class="px-4 py-3 text-sm text-center text-gray-700">${order.merchantTradDate}</td>
      <td class="px-4 py-3 text-sm text-center">
        ${orderSpanElement}</td>
      <td class="px-4 py-3 text-sm text-center text-gray-700">${order.totalAmount}</td>
      <td class="px-4 py-3 text-sm text-center text-gray-700">${orderDetailsContent}</td>
      <td class="px-4 py-3 text-center">
        <button class="check-btn px-3 py-1 text-sm text-blue-600 bg-blue-100 rounded hover:bg-blue-200">查看</button>
        ${cancelButtonElement}
      </td>
    </tr>`;
}

// 設置分頁
let paginationInitialized = false;
const maxVisiblePages = 10;

function updatePagination(totalPages, currentPage, filterCallback) {
  const paginationElement = document.getElementById("pagination");

  paginationElement.innerHTML = "";

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

  setupPagination(totalPages, filterCallback);
}

// 建立分頁按鈕
function createPaginationButton(page, label) {
  const button = document.createElement("li");
  button.innerHTML = `
    <button data-page="${page}" class="px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-400">${label}</button>
  `;
  return button;
}

function setupPagination(totalPages, filterCallback) {
  if (paginationInitialized) return;

  const paginationElement = document.getElementById("pagination");

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

  paginationInitialized = true; // 標記為已初始化
}

