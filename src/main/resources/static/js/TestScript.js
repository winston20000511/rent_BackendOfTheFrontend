
const apiUrltest = 'http://localhost:8080/api/test';

export async function testFetch(){
	
	fetch(apiUrltest).then(response =>{
		if (!response.ok){
			throw new Error('Network response');	
		}
		return response.json();
	}).then(data =>{
		console.log('Data received:' , data);
	}).catch(error =>{
		console.error('Three has been a proble with your fetch operation',error)
	})
}

export function errorCatch(callback,moduleName){
	try {
		callback();
		console.log(`${moduleName}初始化成功`)
	} catch (error) {
		console.log(`${moduleName} 初始化失敗`, error)
	}

}

export function addEvent(selector , eventType , callback){
	const elements = document.querySelectorAll(selector);
	if (elements.length ===0){
		console.error(`選擇器 "${selector}" 匹配不到`)
		return;
	}

	elements.forEach(element => {
		element.addEventListener(eventType,callback);
	})

}

// doSomething().then(function(result){
// 	return doSomethingElse(result);})
// 	.then(function(newResult){
// 	return doThirdThing(newResult);})
// 	.then(function(finalResult){
// 	console.log('final result:' + finalResult)})

