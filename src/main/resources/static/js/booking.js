$(document).ready(function() {
	// 從網址中獲得 houseId
	var url = window.location.pathname.split('/');
	var houseId = url[url.length - 1];

	$.ajax({
		url: '/booking/timeSlot',
		type: 'GET',
		dataType: 'json',
		data: {
			houseId: houseId,
		},
		success: function(response) {
			console.log(response);
			var weekDay = response.weekDay;
			var fromTime = response.fromTime; // "09:00:00"
			var toTime = response.toTime; // "21:00:00"
			var duration = response.duration; // 30

			generateTimeSlots(duration);
			disableUnavailableTimes(fromTime, toTime, duration, weekDay);



			$(".owl").timepicker({
				
			});

			$("#calendar").datepicker({
				inline: true,
				firstDay: 1,
				showOtherMonths: true,
				onChangeMonthYear: function() {
					todayEqualActive();
				},
				onSelect: function(dateText, inst) {
					var date = $(this).datepicker("getDate"),
						day = date.getDate(),
						month = date.getMonth() + 1,
						year = date.getFullYear();

					// display day and month on submit button
					var monthName = months[month - 1];
					$(".request .day").text(year + "年 " + monthName + " " + day + "日");

					todayEqualActive();

					$(".request").removeClass("disabled");

					var index;

					setTimeout(function() {
						$(".ui-datepicker-calendar tbody tr").each(function() {
							if ($(this).find(".ui-datepicker-current-day").length) {
								index = $(this).index() + 1;
							}
						});

						// insert timepiker placeholder after selected row
						$("<tr class='timepicker-cf'></tr>").insertAfter(
							$(".ui-datepicker-calendar tr").eq(index)
						);

						var top = $(".timepicker-cf").offset().top - 2;

						if ($(".timepicker").css("height") == "60px") {
							$(".timepicker-cf").animate(
								{
									height: "0px",
								},
								{ duration: 200, queue: false }
							);
							$(".timepicker").animate(
								{
									top: top,
								},
								200
							);
							$(".timepicker-cf").animate(
								{
									height: "60px",
								},
								200
							);
						} else {
							$(".timepicker").css("top", top);
							$(".timepicker, .timepicker-cf").animate(
								{
									height: "60px",
								},
								200
							);
						}
					}, 0);

					// 點擊時段,顯示時間
					$(".owl-item").on("click", function() {
						// 移除所有項目的 center 標記
						$(".owl-item").removeClass("center");

						// 將被點擊的項目設置為 center
						$(this).addClass("center");

						// 更新顯示的時間
						var time = $(this).text();
						$(".request .time").text(time);

						// 高亮顯示相鄰的項目
						$(".owl-item").removeClass("center-n"); // 清除之前的高亮
						$(this).prev(".owl-item").addClass("center-n"); // 高亮前一項
						$(this).next(".owl-item").addClass("center-n"); // 高亮下一項
					});
				},
			});
		},
		error: function(xhr, status, error) {
			console.error('AJAX 錯誤:', status, error);
		}
	});

	function disableUnavailableTimes(fromTime, toTime, duration, weekDay) {
		// 將所有時間設置為可選
		$(".owl div").removeClass("disabled");

		// 將 fromTime 和 toTime 轉換為小時
		var startHour = parseInt(fromTime.split(':')[0]);
		var endHour = parseInt(toTime.split(':')[0]);
		// 禁用指定的時間段
		for (var hour = startHour; hour < endHour; hour++) {
			for (var minute = 0; minute < 60; minute += duration) {
				var timeSlot = hour.toString().padStart(2, '0') + ':' + minute.toString().padStart(2, '0');
				$(".owl div").each(function() {
					if ($(this).text() === timeSlot) {
						$(this).addClass("disabled");
					}
				});
			}
		}

		// 根據 weekDay 禁用對應的星期
		for (var i = 0; i < weekDay.length; i++) {
			if (weekDay[i] === '0') { // 如果該位為 '0'，則禁用對應的天
				for (var hour = 6; hour <= 21; hour++) { // 假設時間範圍是從06:00到21:00
					$(".owl div").each(function() {
						if ($(this).text().startsWith(hour.toString().padStart(2, '0'))) {
							$(this).addClass("disabled");
						}
					});
				}
			}
		}
	}

	function generateTimeSlots(duration) {
		var timeSlotsContainer = $(".owl");
		timeSlotsContainer.empty(); // 清空之前的時間槽

		for (var hour = 6; hour < 21; hour++) {
			for (var minute = 0; minute < 60; minute += duration) {
				var startTime = hour.toString().padStart(2, '0') + ':' + minute.toString().padStart(2, '0');

				// 計算結束時間
				var endMinute = minute + duration;
				var endHour = hour;

				// 如果結束分鐘等於60，則將其設置為00並將小時加1
				if (endMinute >= 60) {
					endMinute -= 60; // 將結束分鐘減去60
					endHour += 1; // 小時加1
				}

				var endTime = endHour.toString().padStart(2, '0') + ':' + endMinute.toString().padStart(2, '0');

				// 確保不超過結束時間
				if (endHour < 21) { // 確保結束小時不超過21
					var timeSlotDiv = $('<div>').text(startTime + ' ~ ' + endTime);
					timeSlotsContainer.append(timeSlotDiv);
				}
			}
		}
	}
});


var time;
var day;
var month;
var year;
var months = [
	"1月",
	"2月",
	"3月",
	"4月",
	"5月",
	"6月",
	"7月",
	"8月",
	"9月",
	"10月",
	"11月",
	"12月",
];
var center;

// datepicker 設定中文化
$.datepicker.regional['zh-TW'] = {
	clearText: '清除',
	closeText: '關閉',
	prevText: '< 上個月',
	nextText: '下個月 >',
	currentText: '今天',
	monthNames: months,
	monthNamesShort: months,
	dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
	dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
	dateFormat: 'yy-mm-dd',
	firstDay: 1
};
$.datepicker.setDefaults($.datepicker.regional['zh-TW']);

// remove border if the selected date is today's date
function todayEqualActive() {
	setTimeout(function() {
		if ($(".ui-datepicker-current-day").hasClass("ui-datepicker-today")) {
			$(".ui-datepicker-today")
				.children(".ui-state-default")
				.css("border-bottom", "0");
		} else {
			$(".ui-datepicker-today")
				.children(".ui-state-default")
				.css("border-bottom", "2px solid rgba(53,60,66,0.5)");
		}
	}, 20);
}

// call the above function on document ready
todayEqualActive();



// if the inputs arent empty force ":focus state"
$(".form-name input").each(function() {
	$(".form-name input").each(function() {
		// 檢查當前輸入框是否有值
		if (this.value) {
			$(this).siblings("label").css({
				"font-size": "0.8em",
				left: ".15rem",
				top: "0%",
			});
		}

		// 使用 input 事件來捕捉所有輸入變化
		$(this).on('input', function() {
			if (this.value) {
				$(this).siblings("label").css({
					"font-size": "0.8em",
					left: ".15rem",
					top: "0%",
				});
			} else {
				$(this).siblings("label").removeAttr("style");
			}
		});
	});
});



//換月份
$(document).on("click", ".ui-datepicker-next", function(e) {
	$(".timepicker-cf").hide(0);
	$(".timepicker").css({
		height: "0",
	});
	e.preventDefault();
	$(".ui-datepicker").animate(
		{
			"-webkit-transform": "translate(100%,0)",
		},
		200
	);
});
//換月份
$(document).on("click", ".ui-datepicker-prev", function() {
	$(".timepicker-cf").hide(0);
	$(".timepicker").css({
		height: "0",
	});
	$(".ui-datepicker").animate(
		{
			transform: "translateX(-100%)",
		},
		200
	);
});

$(window).on("resize", function() {
	$(".timepicker").css("top", $(".timepicker-cf").offset().top - 2);
});


