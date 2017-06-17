namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class datamigration2 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.University", "CountryId", "dbo.Country");
            DropIndex("dbo.University", new[] { "CountryId" });
        }
        
        public override void Down()
        {
            CreateIndex("dbo.University", "CountryId");
            AddForeignKey("dbo.University", "CountryId", "dbo.Country", "Id", cascadeDelete: true);
        }
    }
}
