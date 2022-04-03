<#import "parts/common.ftlh" as c>

<@c.page>
<h3 class="result">${save}</h3>
<h3 class="result">${message}</h3><br/>
<form action="/gameplay/final-result" method="post">
    <fieldset>
        <legend class="green">Хотите сохранить Ваши рейтинги в базу данных?</legend>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="saveR" id="saveR1" required value="Да">
            <label  class="form-check-label" for="saveR1"><h5>Да</h5></label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="saveR" id="saveR2" required value="Нет">
            <label  class="form-check-label" for="saveR2"><h5>Нет</h5></label>
        </div>
    </fieldset><br/>
    <div class="col-sm-6 mb-3"><button type="submit" class="btn btn-primary">Отправить ответ</button></div>
</form>
</@c.page>

