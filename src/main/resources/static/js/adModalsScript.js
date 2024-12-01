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
  const paidDateYYMMDD = paidDate? paidDate.substring(0, 10) : "無";
  const shownOrderId = orderId? orderId : "無";

  document.getElementById("ad-details-body").innerHTML=
  `
    <tbody id="ad-details-body">
      <tr class="text-center">
        <td>${adId}</td>
        <td>${houseTitle}</td>
        <td>${adName}</td>
        <td>${adPrice}</td>
        <td>${paymentStatus}</td>
        <td>${shownOrderId}</td>
        <td>${paidDateYYMMDD}</td>
      </tr>
    </tbody>
  `

  adDetailModal.addEventListener("click", handleAdDetailModalClose);
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