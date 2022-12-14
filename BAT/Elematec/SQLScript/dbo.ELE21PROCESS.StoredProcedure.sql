USE [ELEMATEC]
GO
/****** Object:  StoredProcedure [dbo].[ELE21PROCESS]    Script Date: 2022/02/21 10:39:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- ********************************************************************************************
-- システム名 : 入出庫管理システム
-- プログラムID : ELE21PROCESS
-- 処理名    : 入庫予定テーブル作成（無償品情報取込
-- 機能概要  : 
-- ********************************************************************************************
CREATE PROCEDURE [dbo].[ELE21PROCESS]
	(
		 @ErrorNumber		int OUTPUT
		,@ErrorMessage		nvarchar(4000) OUTPUT
	)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	DECLARE @rtnCode	int;

	DECLARE @Num		int;
	DECLARE	@return_value int
	DECLARE @GetNumberKubn	VARCHAR(10);
	DECLARE @GetYear	int;
	DECLARE @GetMonth	int;
	DECLARE @OperatorId varchar(10);

	--	無償品ワークTemp格納エリア
	DECLARE @HachuNo	varchar(10);
	DECLARE @EigyoCode	varchar(4);
	DECLARE @EigyoName	nvarchar(30);
	DECLARE @TokuisakiName	nvarchar(35);
	DECLARE	@HinmokuCode	varchar(18);
	DECLARE	@HinmokuName	nvarchar(40);
	DECLARE @HanbaiTani		varchar(3);
	DECLARE @HachuTaniSu	numeric(17,3);
	DECLARE @KihonSuTani	varchar(3);
	DECLARE @Tanaban		varchar(10);
	DECLARE @EigyoTantoName nvarchar(30);
	DECLARE @SiiresakiHinmokuCode nvarchar(35);

    DECLARE @tranStarted bit

    set @tranStarted = 0

	Set @GetNumberKubn = 'MUSHOUHIN';
	Set @GetYear = YEAR(SYSDATETIME());
	Set @GetMonth = MONTH(SYSDATETIME());
	Set @OperatorId = 'IMPORT';
	
	SET @rtnCode = 0;
	SET @ErrorNumber = 0;
	SET @ErrorMessage = N'';

	BEGIN TRY

		--	無償品ワークTempカーソル定義
		DECLARE msh_cur CURSOR LOCAL FOR
			SELECT
				ms.EIGYO_CODE
				,ms.EIGYO_NAME
				,ms.TOKUISAKI_NAME
				,ms.HINMOKU_CODE
				,ms.HINMOKU_NAME
				,ms.HANBAI_TANI
				,ms.HACHU_TANI_SU
				,ms.KIHON_SU_TANI
				,ms.TANABAN
				,ms.EIGYO_TANTOU_NAME
				,ms.SIIRESAKI_HINMOKU_CODE
			FROM
				W_MUSHOUHIN_TEMP ms
		;

		--	カーソルOPEN
		OPEN msh_cur;

		--	カーソルFetch
		FETCH NEXT FROM msh_cur
			INTO
				@EigyoCode
				,@EigyoName
				,@TokuisakiName
				,@HinmokuCode
				,@HinmokuName
				,@HanbaiTani
				,@HachuTaniSu
				,@KihonSuTani
				,@Tanaban
				,@EigyoTantoName
				,@SiiresakiHinmokuCode
			;

	    if ( @@TRANCOUNT = 0 )
			BEGIN
				BEGIN TRANSACTION
				set @tranStarted = 1
			END

		--	ステータステーブル削除
		DELETE FROM T_STATUS
		WHERE
		EXISTS
		(
			SELECT 1
			FROM
			(
				SELECT
					*
				FROM
					T_NYUKOYOTEI A
				WHERE
					A.TORIKOMI_KBN = '2'
				AND
					EXISTS
					(
						SELECT 1
						FROM
							W_MUSHOUHIN_TEMP B
						WHERE
							B.EIGYO_CODE = A.EIGYO_CODE
						AND
							B.HINMOKU_CODE = A.HINMOKU_CODE
						AND
							B.HINMOKU_NAME = A.HINMOKU_NAME
					)
			) SUB
			WHERE
				SUB.INNERJOIN_KEY = T_STATUS.INNERJOIN_KEY
		)

		-- 入庫予定テーブル削除
		DELETE FROM T_NYUKOYOTEI
		WHERE
			NOT EXISTS
				(
					SELECT 1
					FROM T_STATUS SUB
					WHERE
						SUB.INNERJOIN_KEY = T_NYUKOYOTEI.INNERJOIN_KEY
				)
		AND
			TORIKOMI_KBN = '2'
		AND
			EXISTS
			(
				SELECT 1
				FROM
					W_MUSHOUHIN_TEMP SUB
				WHERE
					SUB.EIGYO_CODE = T_NYUKOYOTEI.EIGYO_CODE
				AND
					SUB.HINMOKU_CODE = T_NYUKOYOTEI.HINMOKU_CODE
				AND
					SUB.HINMOKU_NAME = T_NYUKOYOTEI.HINMOKU_NAME
			)

		-- カーソル  ループ
		WHILE @@FETCH_STATUS = 0
		BEGIN
			--	発注番号採番
			EXEC	@return_value = [dbo].[ELE90PROCESS]
					@NumberKubun = @GetNumberKubn,
					@Year = @GetYear,
					@Month = @GetMonth,
					@MaxNumber = 9999,
					@OperatorId = @OperatorId,
					@Num = @Num OUTPUT

			SET @HachuNo = FORMAT(@GetYear, '0000') + FORMAT(@GetMonth, '00') + FORMAT(@Num, '0000');

			--	入庫予定作成
			INSERT INTO [dbo].[T_NYUKOYOTEI]
					   ([HACHU_NO]
					   ,[EIGYO_CODE]
					   ,[EIGYO_NAME]
					   ,[TOKUISAKI_NAME]
					   ,[HINMOKU_CODE]
					   ,[HINMOKU_NAME]
					   ,[HANBAI_TANI]
					   ,[HACHU_TANI_SU]
					   ,[KIHON_SU_TANI]
					   ,[TANABAN]
					   ,[EIGYO_TANTOU_NAME]
					   ,[SIIRESAKI_HINMOKU_CODE]
					   ,[TORIKOMI_KBN]
					   ,[PARTITIONKEY]
					   ,[REGIST_DATE]
					   ,[REGIST_NAME]
					   ,[UPDATE_DATE]
					   ,[UPDATE_NAME])
			SELECT
				@HachuNo
				,@EigyoCode
				,@EigyoName
				,@TokuisakiName
				,@HinmokuCode
				,@HinmokuName
				,@HanbaiTani
				,@HachuTaniSu
				,@KihonSuTani
				,@Tanaban
				,@EigyoTantoName
				,@SiiresakiHinmokuCode
				,'2'
				,FORMAT(SYSDATETIME(), 'yyyyMM')
				,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
				,@OperatorId
				,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
				,@OperatorId

			FETCH NEXT FROM msh_cur
			INTO
				@EigyoCode
				,@EigyoName
				,@TokuisakiName
				,@HinmokuCode
				,@HinmokuName
				,@HanbaiTani
				,@HachuTaniSu
				,@KihonSuTani
				,@Tanaban
				,@EigyoTantoName
				,@SiiresakiHinmokuCode
			;
		END
		-- カーソル クローズ
		CLOSE msh_cur;

		--	ステータステーブルの作成
		INSERT INTO [dbo].[T_STATUS]
				   ([HACHU_NO]
				   ,[HACHU_EDA]
				   ,[DSTATUS]
				   ,[REGIST_DATE]
				   ,[REGIST_NAME]
				   ,[UPDATE_DATE]
				   ,[UPDATE_NAME])
		SELECT
			MAIN.HACHU_NO
			,0
			,'01'
			,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
			,'IMPORT'
			,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
			,@OperatorId
		FROM
			T_NYUKOYOTEI MAIN
		WHERE
			MAIN.INNERJOIN_KEY IS NULL
		AND
			MAIN.TORIKOMI_KBN = '2'


		--	入庫予定テーブルの更新
		UPDATE
			MAIN
		SET
			INNERJOIN_KEY = SUB.INNERJOIN_KEY
			,UPDATE_DATE = FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
			,UPDATE_NAME = @OperatorId
		FROM
			T_NYUKOYOTEI MAIN
		INNER JOIN
			T_STATUS SUB
		ON
			MAIN.HACHU_NO = SUB.HACHU_NO
		AND
			SUB.DSTATUS = '01'
		WHERE
			MAIN.INNERJOIN_KEY IS NULL

		if ( @tranStarted = 1 )
		BEGIN
			COMMIT TRANSACTION
		END

		SET @rtnCode = 1;
	END TRY
	BEGIN CATCH
		SET @ErrorNumber = ERROR_NUMBER();
		SET @ErrorMessage = ERROR_MESSAGE();
		SET @rtnCode = 9;
		if ( @tranStarted = 1 )
		BEGIN
			ROLLBACK TRANSACTION
		END
	END CATCH

	return @rtnCode;

END
GO
