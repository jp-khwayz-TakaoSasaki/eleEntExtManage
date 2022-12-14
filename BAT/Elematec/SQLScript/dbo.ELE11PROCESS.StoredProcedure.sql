USE [ELEMATEC]
GO
/****** Object:  StoredProcedure [dbo].[ELE11PROCESS]    Script Date: 2022/02/17 14:35:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- ********************************************************************************************
-- システム名 : 入出庫管理システム
-- プログラムID : ELE11PROCESS
-- 処理名    : 入庫予定テーブル作成（入庫事前連絡情報取込
-- 機能概要  : 
-- ********************************************************************************************
CREATE PROCEDURE [dbo].[ELE11PROCESS] 
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
	DECLARE @OperatorId varchar(10);
	
	SET @rtnCode = 0;
	SET @ErrorNumber = 0;
	SET @ErrorMessage = N'';
	Set @OperatorId = 'IMPORT';

	BEGIN TRY
		BEGIN TRANSACTION

		--	ステータステーブルの削除
		DELETE FROM T_STATUS
		WHERE
			EXISTS
				(
					SELECT 1
					FROM T_NYUKOYOTEI SUB
					WHERE
						SUB.INNERJOIN_KEY = T_STATUS.INNERJOIN_KEY
					AND
						SUB.TORIKOMI_KBN = '1'
				)
		AND
			DSTATUS = '01'

		--	入庫予定テーブルの削除１
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
			TORIKOMI_KBN = '1'

		--	入庫予定テーブルの削除２　※ステータステーブルが03にならないため処理不要
		--DELETE FROM T_NYUKOYOTEI
		--WHERE
		--	EXISTS
		--		(
		--			SELECT 1
		--			FROM T_STATUS SUB
		--			WHERE
		--				SUB.INNERJOIN_KEY = T_NYUKOYOTEI.INNERJOIN_KEY
		--			AND
		--				SUB.DSTATUS >= '03'
		--		)
		--AND
		--	TORIKOMI_KBN = '1'

		--	ステータステーブルの削除２
		DELETE FROM T_STATUS
		WHERE
			NOT EXISTS
				(
					SELECT 1
					FROM T_NYUKOYOTEI SUB
					WHERE
						SUB.INNERJOIN_KEY = T_STATUS.INNERJOIN_KEY
				)
		AND
			DSTATUS = '01'


		--	入庫予定テーブルの作成
		INSERT INTO [dbo].[T_NYUKOYOTEI]
				   ([HACHU_NO]
				   ,[JUCHU_NO]
				   ,[EIGYO_CODE]
				   ,[EIGYO_NAME]
				   ,[TOKUISAKI_NAME]
				   ,[HINMOKU_CODE]
				   ,[HINMOKU_NAME]
				   ,[TOKUISAKI_HINMOKU]
				   ,[HANBAI_TANI]
				   ,[JUCHU_TANI_SU]
				   ,[JUCHU_KANZAN_BUNBO]
				   ,[JUCHU_KANZAN_BUNSI]
				   ,[HACHU_TANI]
				   ,[HACHU_TANI_SU]
				   ,[KIHON_SU_TANI]
				   ,[HACHU_KANZAN_BUNBO]
				   ,[HACHU_KANZAN_BUNSI]
				   ,[KIKEN_CODE]
				   ,[KOSEI_BUSITU]
				   ,[TANABAN]
				   ,[TOKUISAKI_HACHU_NO]
				   ,[KYOTEN_CODE]
				   ,[KYOTEN_NAME]
		           ,[EIGYO_TANTOU_NAME]
				   ,[SIIRESAKI_HINMOKU_CODE]
				   ,[TORIKOMI_KBN]
				   ,[PARTITIONKEY]
				   ,[INNERJOIN_KEY]
				   ,[REGIST_DATE]
				   ,[REGIST_NAME]
				   ,[UPDATE_DATE]
				   ,[UPDATE_NAME])
		SELECT
			MAIN.HACHU_NO
			,MAIN.JUCHU_NO
			,MAIN.EIGYO_CODE
			,MAIN.EIGYO_NAME
			,MAIN.TOKUISAKI_NAME
			,MAIN.HINMOKU_CODE
			,MAIN.HINMOKU_NAME
			,MAIN.TOKUISAKI_HINMOKU
			,MAIN.HANBAI_TANI
			,MAIN.JUCHU_TANI_SU
			,MAIN.JUCHU_KANZAN_BUNBO
			,MAIN.JUCHU_KANZAN_BUNSI
			,MAIN.HACHU_TANI
			,MAIN.HACHU_TANI_SU
			,MAIN.KIHON_SU_TANI
			,MAIN.HACHU_KANZAN_BUNBO
			,MAIN.HACHU_KANZAN_BUNSI
			,MAIN.KIKEN_CODE
			,MAIN.KOSEI_BUSITU
			,MAIN.TANABAN
			,MAIN.TOKUISAKI_HACHU_NO
			,MAIN.KYOTEN_CODE
			,MAIN.KYOTEN_NAME
            ,MAIN.EIGYO_TANTOU_NAME
			,MAIN.SIIRESAKI_HINMOKU_CODE
			,'1'
			,FORMAT(SYSDATETIME(), 'yyyyMM')
			,null
			,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
			,@OperatorId
			,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
			,@OperatorId
		FROM W_NYUKOYOTEI_TEMP MAIN
		WHERE
			NOT EXISTS
			(
				SELECT 1
				FROM T_NYUKOYOTEI SUB
				WHERE SUB.HACHU_NO = MAIN.HACHU_NO
			)

		--	ステータステーブルの作成
		INSERT INTO [dbo].[T_STATUS]
				   ([HACHU_NO]
				   ,[HACHU_EDA]
				   ,[DSTATUS]
--				   ,[INNERJOIN_KEY]
--				   ,[KYOTEN_CD]
				   ,[REGIST_DATE]
				   ,[REGIST_NAME]
				   ,[UPDATE_DATE]
				   ,[UPDATE_NAME])
		SELECT
			MAIN.HACHU_NO
			,0
			,'01'
			,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
			,@OperatorId
			,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
			,@OperatorId
		FROM
			T_NYUKOYOTEI MAIN
		WHERE
			MAIN.INNERJOIN_KEY IS NULL
		AND
			MAIN.TORIKOMI_KBN = '1'

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


		COMMIT TRANSACTION
		SET @rtnCode = 1;
	END TRY
	BEGIN CATCH
		SET @ErrorNumber = ERROR_NUMBER();
		SET @ErrorMessage = ERROR_MESSAGE();
		SET @rtnCode = 9;
		ROLLBACK TRANSACTION
	END CATCH

	return @rtnCode;
END
GO
