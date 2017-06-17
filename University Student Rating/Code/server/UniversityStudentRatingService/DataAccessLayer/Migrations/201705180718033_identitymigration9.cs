namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class identitymigration9 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.AspNetUsers", "UserProfileId", c => c.Int(nullable: false));
            CreateIndex("dbo.AspNetUsers", "UserProfileId");
            AddForeignKey("dbo.AspNetUsers", "UserProfileId", "dbo.UserProfile", "Id", cascadeDelete: true);
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.AspNetUsers", "UserProfileId", "dbo.UserProfile");
            DropIndex("dbo.AspNetUsers", new[] { "UserProfileId" });
            DropColumn("dbo.AspNetUsers", "UserProfileId");
        }
    }
}
