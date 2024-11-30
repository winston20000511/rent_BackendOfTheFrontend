/* 廣告詳細內容的彈跳視窗 */
const adDetailModal = document.getElementById("ad-detail-modal");

/* 成功新增廣告後跳出的提示視窗 */
const succeessModal = document.getElementById("success-modal");
const modalBackground = document.getElementById("modal-background");

/* 處理廣告詳細內容 */
export function showAdDetailsModal(adDetails) {
  adDetailModal.classList.remove("hidden");
  toggleModalBackground();

  const {adId, adName, adPrice, houseTitle, isPaid, orderId, paidDate, userId, userName} = adDetails;

  // 處理顯示文字
  const paymentStatus = isPaid? "已付款" : "未付款";
  const paidDateYYYYMMDD = paidDate? paidDate.substring(0, 10) : "無";
  const shownOrderId = orderId? orderId : "無";

  // 處理廣告起訖日
  const adDurationMap = {
    "30天": 30,
    "60天": 60
  };
  const daysToAdd = adDurationMap[adName] || 0;
  const endDateYYYYDDMM = addDaysToDate(paidDate, daysToAdd);

  let period = paidDate? `${paidDateYYYYMMDD} ~ ${endDateYYYYDDMM}` : "無";

  document.getElementById("ad-details-body").innerHTML=
  `
    <table class="border border-gray-400 w-full">
      <tbody id="ad-details-body" class="text-center">
        <tr class="px-2 py-4">
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">廣告編號</th>
          <td class="border border-gray-400">${adId}</td>
        </tr>
        <tr>
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">房屋標題</th>
          <td class="border border-gray-400">${houseTitle}</td>
        </tr>
        <tr>
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">廣告種類</th>
          <td class="border border-gray-400">${adName}</td>
        </tr>
        <tr>
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">廣告金額</th>
          <td class="border border-gray-400">${adPrice}</td>
        </tr>
        <tr>
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">付款狀態</th>
          <td class="border border-gray-400">${paymentStatus}</td>
        </tr>
        <tr>
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">訂單號碼</th>
          <td class="border border-gray-400">${shownOrderId}</td>
        </tr>
        <tr>
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">付款日期</th>
          <td class="border border-gray-400">${paidDateYYYYMMDD}</td>
        </tr>
        <tr>
          <th class="bg-gray-200 w-1/4 border border-gray-400 px-2 py-2">起訖日期</th>
          <td class="border border-gray-400">${period}</td>
        </tr>
    </tbody>`;

  adDetailModal.addEventListener("click", handleAdDetailModalClose);

  // 建置修改按鈕
}

function handleAdDetailModalClose(event){
	event.stopPropagation();
	if(event.target.id === "close-ad-modal-btn"){
	    adDetailModal.classList.add("hidden");
	    toggleModalBackground();
	}
}

/* 處理成功提視窗 */
// 顯示提視窗
export function showSuccessAddAdsModal() {
  succeessModal.classList.remove("hidden");
}

// 點擊指定按鈕，前往指定視窗
export function clickModalBtn(onclickModalBtn){
  const modalBtns = document.querySelectorAll(".modal-btn");

  modalBtns.forEach((button) => {
    button.addEventListener("click", function(event){
        event.stopPropagation();
        onclickModalBtn(button);
    });
  });
}

// 關閉成功新增的提示
export function closeModalBtn(){
  succeessModal.classList.add("hidden");
}

// 開關模糊背景
function toggleModalBackground() {
   modalBackground.classList.toggle("hidden");
   modalBackground.classList.toggle("overlay");
}

// 計算起訖日
function addDaysToDate(startDate, daysToAdd){
  const resultDate = new Date(startDate);
  resultDate.setDate(resultDate.getDate() + daysToAdd);

  const formattedDate = resultDate.toISOString().substring(0,10);
  return formattedDate;
}

// 之後再想
/*
// 1. 留在新增廣告的頁面
export function stayAtAddAd() {
  succeessModal.classList.add("hidden");
  console.log(modalBackground.classList.contains("hidden"));
}

// 2. 前往未確認廣告的頁面
export function goCheckUnpaidAds() {
  succeessModal.classList.add("hidden");
  addAdTable.classList.add("hidden");
  unPaidAdTable.classList.remove("hidden");
  addAdBtn.classList.remove("bg-blue-700");
  addAdBtn.classList.add("bg-blue-500");
  unPaidAdsBtn.classList.add("bg-blue-700");
}
*/