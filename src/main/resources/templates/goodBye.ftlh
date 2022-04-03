<#import "parts/common.ftlh" as c>

<@c.page>
    <#if message??>
        <p class="green"><strong>${message}</strong></p><br><br>
    </#if>
    <form action="/gameplay" method="get">
        <h5 class="result">${continue}</h5><br/>
        <div class="col-sm-6 mb-3">
            <button type="submit" class="btn btn-primary">Новая игра</button>
        </div>
    </form>
    <div class="line"></div><br/>
    <div class="container">
        <div class="row">
            <div class="col order-first">
                <div class="form-group">
                    <#if rating??>
                        <h5 class="gray">Рейтинг игроков:</h5><br/>
                        <table border="2">
                            <thead>
                            <tr bgcolor="#d3d3d3">
                                <th scope="col">Имя игрока</th>
                                <th scope="col">Рейтинг</th>
                            </tr>
                            </thead>
                            <#list rating as r>
                                <tbody>
                                <tr>
                                    <td>${r.name}</td>
                                    <td>${r.rating}</td>
                                </tr>
                                </tbody>
                            </#list>
                        </table>
                    <#else>
                        <h5 class="result"> Рейтинг пуст! Сыграйте в игру с сохранением рейтинга и он здесь появится
                            :)</h5>
                    </#if>
                </div>
            </div>
            <div>
                <form action="/gameplay/final-result" method="get">
                    <div class="col-sm-10 mb-3">
                        <#if gameHistoryIsEmpty??>
                            <h5 class="result">${gameHistoryIsEmpty}</h5>
                        </#if>
                        <#if gameHistoryNotEmpty??>
                            <h5 class="result">${gameHistoryNotEmpty}</h5>
                        </#if><br/>
                        <div>
                        <input type="number" class="form-control"
                               id="id" name="id" placeholder="Id истории игры" autofocus
                               value="<#if id??>${id}</#if>" required/><br/>
                        <button type="submit" class="btn btn-primary">Поиск и воспроизведение истории игры по id
                        </button>
                        </div>
                    </div>
                </form>
            </div>
            <br/>
            <div class="col">
                <#if gameHistory??>
                    <h5 class="result">История игры c id = ${gameHistory.id}:</h5><br/>
                    <h6 class="history">${gameHistory.firstPlayer}</h6>
                    <h6 class="history">${gameHistory.secondPlayer}</h6>
                    <#list steps as step>
                        <h6 class="history">${step}</h6>
                    </#list>
                    <h6 class="history">${gameHistory.gameResult}</h6>
                </#if>
                <#if gameHistoryNotFound??>
                    <h6 class="red">${gameHistoryNotFound}</h6>
                </#if>
            </div>
        </div>
    </div>
</@c.page>

