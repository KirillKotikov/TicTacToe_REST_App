<#import "parts/common.ftlh" as c>

<@c.page>
    <a><strong>Игровое поле:</strong></a><br><br>
    <div>
        <#list field as f>
            <h3>${f}</h3>
        </#list>
    </div>
    <br>
    <h3 class="result">${message1}</h3>
    <h3 class="result">${message2}</h3>
    <h3 class="result">${message3}</h3>
    <h3 class="result">${message4}</h3>
    <br/>
    <form action="/gameplay/result" method="post">
        <fieldset>
            <legend class="green">Вы хотите сохранить историю данной игры в базу данных?</legend>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="saveH" id="saveH1" required value="Да">
                <label  class="form-check-label" for="saveH1"><h5>Да</h5></label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="saveH" id="saveH2" required value="Нет">
                <label  class="form-check-label" for="saveH2"><h5>Нет</h5></label>
            </div>
        </fieldset>
        <fieldset>
            <legend class="green">Хотите сыграть еще разок?)</legend>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="newGame" id="newGame1" required value="Да">
                <label  class="form-check-label" for="newGame1"><h5>Да</h5></label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="newGame" id="newGame2" required value="Нет">
                <label  class="form-check-label" for="newGame2"><h5>Нет</h5></label>
            </div>
        </fieldset>
        <button type="submit" class="btn btn-primary">Отправить ответы</button>
    </form>
</@c.page>